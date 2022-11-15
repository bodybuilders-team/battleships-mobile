package pt.isel.pdm.battleships.ui.screens.gameplay.gameplay

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import pt.isel.pdm.battleships.SessionManager
import pt.isel.pdm.battleships.domain.games.Coordinate
import pt.isel.pdm.battleships.domain.games.board.MyBoard
import pt.isel.pdm.battleships.domain.games.board.OpponentBoard
import pt.isel.pdm.battleships.domain.games.game.GameConfig
import pt.isel.pdm.battleships.domain.games.ship.Orientation
import pt.isel.pdm.battleships.domain.games.ship.Ship
import pt.isel.pdm.battleships.domain.games.ship.ShipType
import pt.isel.pdm.battleships.services.BattleshipsService
import pt.isel.pdm.battleships.services.LinkBattleshipsService
import pt.isel.pdm.battleships.services.games.models.players.fireShots.FireShotsInput
import pt.isel.pdm.battleships.services.games.models.players.ship.GetFleetOutputModel
import pt.isel.pdm.battleships.services.games.models.players.shot.FiredShotModel
import pt.isel.pdm.battleships.services.games.models.players.shot.UnfiredShotModel
import pt.isel.pdm.battleships.ui.screens.gameplay.gameplay.GameplayViewModel.GameplayState.IDLE
import pt.isel.pdm.battleships.ui.screens.gameplay.gameplay.GameplayViewModel.GameplayState.LOADING_GAME
import pt.isel.pdm.battleships.ui.screens.gameplay.gameplay.GameplayViewModel.GameplayState.LOADING_MY_FLEET
import pt.isel.pdm.battleships.ui.screens.gameplay.gameplay.GameplayViewModel.GameplayState.PLAYING_GAME
import pt.isel.pdm.battleships.ui.utils.Event
import pt.isel.pdm.battleships.ui.utils.executeRequestRetrying
import pt.isel.pdm.battleships.ui.utils.launchAndExecuteRequestRetrying
import pt.isel.pdm.battleships.ui.utils.navigation.Rels

/**
 * View model for the [GameplayActivity].
 */
class GameplayViewModel(
    private val battleshipsService: BattleshipsService,
    private val sessionManager: SessionManager
) : ViewModel() {

    /**
     * The state of the [GameplayScreen].
     *
     * @property state the current state of the screen
     * @property gameConfig the game configuration, null if the game is not loaded
     * @property myBoard the board of the player, null if the game is not loaded
     * @property opponentBoard the board of the opponent, null if the game is not loaded
     * @property myTurn true if it's the player's turn, false otherwise, null if the game is not loaded
     */
    data class GameplayScreenState(
        val state: GameplayState = IDLE,
        val gameConfig: GameConfig? = null,
        val myBoard: MyBoard? = null,
        val opponentBoard: OpponentBoard? = null,
        val myTurn: Boolean? = null
    )

    private var _screenState by mutableStateOf(GameplayScreenState())
    val screenState: GameplayScreenState
        get() = _screenState

    private val _events = MutableSharedFlow<Event>()
    val events: SharedFlow<Event> = _events

    private lateinit var linksBattleshipsService: LinkBattleshipsService
    private val linksGamesService by lazy { linksBattleshipsService.gamesService }
    private val linksPlayersService by lazy { linksBattleshipsService.playersService }

    /**
     * Loads the game.
     *
     * @param gameLink the link to the game
     */
    fun loadGame(gameLink: String) {
        check(_screenState.state == IDLE) { "The view model is not in idle state." }
        _screenState = _screenState.copy(state = LOADING_GAME)

        linksBattleshipsService = LinkBattleshipsService(
            links = mapOf(Rels.GAME to gameLink),
            sessionManager = sessionManager,
            battleshipsService = battleshipsService
        )

        launchAndExecuteRequestRetrying(
            request = { linksGamesService.getGame() },
            events = _events,
            onSuccess = { gameData ->
                _screenState = _screenState.copy(state = GameplayState.GAME_LOADED)

                getMyFleet()

                check(_screenState.state == GameplayState.MY_FLEET_LOADED) {
                    "The view model is not in my fleet loaded state."
                }

                val properties = gameData.properties
                    ?: throw IllegalStateException("No game properties found")

                val turn = properties.state.turn
                    ?: throw IllegalStateException("No turn found")

                val gameConfig = GameConfig(properties.config)

                val myTurn = turn == sessionManager.username
                _screenState = _screenState.copy(
                    gameConfig = gameConfig,
                    state = PLAYING_GAME,
                    opponentBoard = OpponentBoard(gameConfig.gridSize),
                    myTurn = myTurn
                )

                if (!myTurn) waitForOpponent()
            }
        )
    }

    /**
     * Gets the player's fleet.
     */
    private suspend fun getMyFleet() {
        check(_screenState.state == GameplayState.GAME_LOADED) {
            "The view model is not in game loaded state."
        }

        _screenState = _screenState.copy(state = LOADING_MY_FLEET)

        val myFleetData = executeRequestRetrying(
            request = { linksPlayersService.getMyFleet() },
            events = _events
        )

        val initialFleet = parseFleet(
            fleet = myFleetData.properties
                ?: throw IllegalStateException("No ships found")
        )

        val gridSize = _screenState.gameConfig?.gridSize
            ?: throw IllegalStateException("No game config found")

        _screenState = _screenState.copy(
            myBoard = MyBoard(gridSize, initialFleet),
            state = GameplayState.MY_FLEET_LOADED
        )
    }

    /**
     * Fires a list of shots.
     *
     * @param coordinates the list of coordinates where to fire
     */
    fun fireShots(coordinates: List<Coordinate>) {
        check(_screenState.state == PLAYING_GAME) { "The game is not in the playing state" }
        check(_screenState.myTurn == true) { "It's not your turn" }

        launchAndExecuteRequestRetrying(
            request = {
                linksPlayersService.fireShots(
                    shots = FireShotsInput(
                        coordinates.map { UnfiredShotModel(it.toCoordinateDTO()) }
                    )
                )
            },
            events = _events,
            onSuccess = { fireShotsData ->
                val firedShots = fireShotsData.properties?.shots
                    ?.map(FiredShotModel::toFiredShot)
                    ?: throw IllegalStateException("No shots found")

                _screenState = _screenState.copy(
                    opponentBoard = _screenState.opponentBoard?.shoot(
                        firedShots.map { shot ->
                            shot.coordinate to
                                (shot.result.result == "HIT" || shot.result.result == "SUNK")
                        }
                    ),
                    myTurn = false
                )

                waitForOpponent()
            }
        )
    }

    /**
     * Waits for the opponent to play.
     */
    private suspend fun waitForOpponent() {
        check(_screenState.state == PLAYING_GAME) { "The game is not in the playing state" }
        check(_screenState.myTurn == false) { "It's not the opponent's turn" }

        val gameStateData = executeRequestRetrying(
            request = { linksGamesService.getGameState() },
            events = _events
        )

        val properties = gameStateData.properties
            ?: throw IllegalStateException("Game state properties are null")

        if (properties.phase == "FINISHED") {
            _screenState = _screenState.copy(state = GameplayState.FINISHED_GAME)
        }

        if (properties.turn != sessionManager.username) { // TODO: add constants
            delay(1000L)
        } else {
            getOpponentShots()
            _screenState = _screenState.copy(myTurn = true)
        }
    }

    /**
     * Gets the opponent's shots.
     */
    private suspend fun getOpponentShots() {
        val opponentShotsData = executeRequestRetrying(
            request = { linksPlayersService.getOpponentShots() },
            events = _events
        )

        val opponentShots = opponentShotsData.properties?.shots
            ?: throw IllegalStateException("No shots found")

        val myBoard = _screenState.myBoard
            ?: throw IllegalStateException("No my board found")

        _screenState = _screenState.copy(
            myBoard = MyBoard(
                size = myBoard.size,
                initialFleet = myBoard.initialFleet
            ).shoot(
                opponentShots.map {
                    it.coordinate.toCoordinate()
                }
            )
        )
    }

    /**
     * Parses a fleet into a list of ships.
     *
     * @param fleet the fleet to parse
     *
     * @return the list of ships
     * @throws IllegalStateException if the fleet is invalid
     */
    private fun parseFleet(fleet: GetFleetOutputModel): List<Ship> =
        fleet.ships.map {
            val shipType = ShipType.values()
                .find { shipType -> shipType.shipName == it.type }
                ?: throw IllegalStateException("Invalid ship type")

            val orientation = Orientation.values()
                .find { orientation -> orientation.name == it.orientation }
                ?: throw IllegalStateException("Invalid ship orientation")

            Ship(
                type = shipType,
                coordinate = it.coordinate.toCoordinate(),
                orientation = orientation,
                lives = shipType.size
            )
        }

    /**
     * The state of the view model.
     *
     * @property LOADING_GAME the view model is loading the game TODO Comment
     */
    enum class GameplayState {
        IDLE,
        LOADING_GAME,
        GAME_LOADED,
        LOADING_MY_FLEET,
        MY_FLEET_LOADED,
        PLAYING_GAME,
        FINISHED_GAME
    }
}
