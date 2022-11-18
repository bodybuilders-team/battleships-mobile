package pt.isel.pdm.battleships.ui.screens.gameplay.gameplay

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay
import pt.isel.pdm.battleships.SessionManager
import pt.isel.pdm.battleships.domain.games.Coordinate
import pt.isel.pdm.battleships.domain.games.board.MyBoard
import pt.isel.pdm.battleships.domain.games.board.OpponentBoard
import pt.isel.pdm.battleships.domain.games.game.GameConfig
import pt.isel.pdm.battleships.domain.games.ship.Orientation
import pt.isel.pdm.battleships.domain.games.ship.Ship
import pt.isel.pdm.battleships.domain.games.ship.ShipType
import pt.isel.pdm.battleships.services.BattleshipsService
import pt.isel.pdm.battleships.services.games.models.games.getGame.GetGameOutput
import pt.isel.pdm.battleships.services.games.models.players.fireShots.FireShotsInput
import pt.isel.pdm.battleships.services.games.models.players.ship.GetFleetOutputModel
import pt.isel.pdm.battleships.services.games.models.players.shot.UnfiredShotModel
import pt.isel.pdm.battleships.ui.screens.BattleshipsViewModel
import pt.isel.pdm.battleships.ui.screens.gameplay.gameplay.GameplayViewModel.GameplayState.IDLE
import pt.isel.pdm.battleships.ui.screens.gameplay.gameplay.GameplayViewModel.GameplayState.LINKS_LOADED
import pt.isel.pdm.battleships.ui.screens.gameplay.gameplay.GameplayViewModel.GameplayState.LOADING_GAME
import pt.isel.pdm.battleships.ui.utils.executeRequestThrowing
import pt.isel.pdm.battleships.ui.utils.launchAndExecuteRequestThrowing
import pt.isel.pdm.battleships.ui.utils.navigation.Links

/**
 * View model for the [GameplayActivity].
 */
class GameplayViewModel(
    battleshipsService: BattleshipsService,
    sessionManager: SessionManager
) : BattleshipsViewModel(battleshipsService, sessionManager) {

    /**
     * The state of the [GameplayScreen].
     *
     * @property gameConfig the game configuration, null if the game is not loaded
     * @property myBoard the board of the player, null if the game is not loaded
     * @property opponentBoard the board of the opponent, null if the game is not loaded
     * @property myTurn true if it's the player's turn, false otherwise, null if the game is not loaded
     */
    data class GameplayScreenState(
        val gameConfig: GameConfig? = null,
        val myBoard: MyBoard? = null,
        val opponentBoard: OpponentBoard? = null,
        val myTurn: Boolean? = null
    )

    private var _screenState by mutableStateOf(GameplayScreenState())
    val screenState: GameplayScreenState
        get() = _screenState

    private var _state: GameplayState by mutableStateOf(IDLE)
    val state
        get() = _state

    /**
     * Loads the game.
     */
    fun loadGame() {
        check(state == LINKS_LOADED) { "The view model is not in links loaded state." }

        _state = LOADING_GAME

        launchAndExecuteRequestThrowing(
            request = { battleshipsService.gamesService.getGame() },
            events = _events,
            onSuccess = { gameData: GetGameOutput ->
                val properties = gameData.properties
                    ?: throw IllegalStateException("No game properties found")

                val turn = properties.state.turn
                    ?: throw IllegalStateException("No turn found")

                val gameConfig = GameConfig(properties.config)
                val myTurn = turn == sessionManager.username

                _screenState = _screenState.copy(
                    gameConfig = gameConfig,
                    opponentBoard = OpponentBoard(gameConfig.gridSize),
                    myTurn = myTurn
                )
                _state = GameplayState.GAME_LOADED

                getMyFleet()

                check(state == GameplayState.MY_FLEET_LOADED) {
                    "The view model is not in my fleet loaded state."
                }

                _state = GameplayState.PLAYING_GAME

                if (!myTurn) waitForOpponent()
            }
        )
    }

    /**
     * Gets the player's fleet.
     */
    private suspend fun getMyFleet() {
        check(state == GameplayState.GAME_LOADED) { "The view model is not in game loaded state." }

        _state = GameplayState.LOADING_MY_FLEET

        val myFleetData = executeRequestThrowing(
            request = { battleshipsService.playersService.getMyFleet() },
            events = _events
        )

        val initialFleet = parseFleet(
            fleet = myFleetData.properties
                ?: throw IllegalStateException("No ships found"),
            shipTypes = _screenState.gameConfig?.ships
                ?: throw IllegalStateException("No game config found")
        )

        val gridSize = _screenState.gameConfig?.gridSize
            ?: throw IllegalStateException("No game config found")

        _screenState = _screenState.copy(myBoard = MyBoard(gridSize, initialFleet))
        _state = GameplayState.MY_FLEET_LOADED
    }

    /**
     * Fires a list of shots.
     *
     * @param coordinates the list of coordinates where to fire
     */
    fun fireShots(coordinates: List<Coordinate>) {
        check(state == GameplayState.PLAYING_GAME) { "The game is not in the playing state" }
        check(_screenState.myTurn == true) { "It's not your turn" }

        launchAndExecuteRequestThrowing(
            request = {
                battleshipsService.playersService.fireShots(
                    shots = FireShotsInput(
                        coordinates.map { UnfiredShotModel(it.toCoordinateModel()) }
                    )
                )
            },
            events = _events,
            onSuccess = { fireShotsData ->
                val shipTypes = _screenState.gameConfig?.ships
                    ?: throw IllegalStateException("No game config found")

                val firedShots = fireShotsData.properties?.shots
                    ?.map { it.toFiredShot(shipTypes) }
                    ?: throw IllegalStateException("No shots found")

                val opponentBoard = _screenState.opponentBoard
                    ?: throw IllegalStateException("No opponent board found")

                _screenState = _screenState.copy(
                    opponentBoard = opponentBoard.updateWith(firedShots)
                )

                delay(TURN_SWITCH_DELAY)

                _screenState = _screenState.copy(myTurn = false)

                waitForOpponent()
            }
        )
    }

    /**
     * Waits for the opponent to play.
     */
    private suspend fun waitForOpponent() {
        check(state == GameplayState.PLAYING_GAME) { "The game is not in the playing state" }
        check(_screenState.myTurn == false) { "It's not the opponent's turn" }

        while (true) {
            val gameStateData = executeRequestThrowing(
                request = { battleshipsService.gamesService.getGameState() },
                events = _events
            )

            val properties = gameStateData.properties
                ?: throw IllegalStateException("Game state properties are null")

            if (properties.phase == FINISHED_PHASE)
                _state = GameplayState.FINISHED_GAME

            if (properties.turn != sessionManager.username) {
                delay(POLLING_DELAY)
            } else {
                getOpponentShots()
                delay(TURN_SWITCH_DELAY)
                _screenState = _screenState.copy(myTurn = true)
                break
            }
        }
    }

    /**
     * Gets the opponent's shots.
     */
    private suspend fun getOpponentShots() {
        val opponentShotsData = executeRequestThrowing(
            request = { battleshipsService.playersService.getOpponentShots() },
            events = _events
        )

        val opponentShots = opponentShotsData.properties?.shots
            ?: throw IllegalStateException("No shots found")

        val myBoard = _screenState.myBoard
            ?: throw IllegalStateException("No my board found")

        _screenState = _screenState.copy(
            myBoard = MyBoard(
                size = myBoard.size,
                initialFleet = myBoard.fleet
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
    private fun parseFleet(fleet: GetFleetOutputModel, shipTypes: List<ShipType>): List<Ship> =
        fleet.ships.map {
            val shipType = shipTypes
                .find { shipType -> shipType.shipName == it.type }
                ?: throw IllegalStateException("Invalid ship type")

            val orientation = Orientation.valueOf(it.orientation)

            Ship(
                type = shipType,
                coordinate = it.coordinate.toCoordinate(),
                orientation = orientation
            )
        }

    /**
     * Updates the links.
     *
     * @param links the links to update
     */
    override fun updateLinks(links: Links) {
        super.updateLinks(links)
        _state = LINKS_LOADED
    }

    /**
     * The state of the view model.
     *
     * @property LOADING_GAME the view model is loading the game TODO Comment
     */
    enum class GameplayState {
        IDLE,
        LINKS_LOADED,
        LOADING_GAME,
        GAME_LOADED,
        LOADING_MY_FLEET,
        MY_FLEET_LOADED,
        PLAYING_GAME,
        FINISHED_GAME
    }

    companion object {
        private const val FINISHED_PHASE = "FINISHED"
        private const val POLLING_DELAY = 500L
        private const val TURN_SWITCH_DELAY = 1500L
    }
}
