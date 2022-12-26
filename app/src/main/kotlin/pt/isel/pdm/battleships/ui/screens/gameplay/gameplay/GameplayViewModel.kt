package pt.isel.pdm.battleships.ui.screens.gameplay.gameplay

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import pt.isel.pdm.battleships.domain.games.Coordinate
import pt.isel.pdm.battleships.domain.games.board.MyBoard
import pt.isel.pdm.battleships.domain.games.board.OpponentBoard
import pt.isel.pdm.battleships.domain.games.game.GameConfig
import pt.isel.pdm.battleships.domain.games.game.GamePhase
import pt.isel.pdm.battleships.domain.games.game.GameState
import pt.isel.pdm.battleships.domain.games.ship.Orientation
import pt.isel.pdm.battleships.domain.games.ship.Ship
import pt.isel.pdm.battleships.domain.games.ship.ShipType
import pt.isel.pdm.battleships.domain.users.Player
import pt.isel.pdm.battleships.service.BattleshipsService
import pt.isel.pdm.battleships.service.services.games.models.games.getGame.GetGameOutput
import pt.isel.pdm.battleships.service.services.games.models.players.fireShots.FireShotsInput
import pt.isel.pdm.battleships.service.services.games.models.players.ship.GetFleetOutputModel
import pt.isel.pdm.battleships.service.services.games.models.players.shot.UnfiredShotModel
import pt.isel.pdm.battleships.session.SessionManager
import pt.isel.pdm.battleships.ui.screens.BattleshipsViewModel
import pt.isel.pdm.battleships.ui.screens.gameplay.gameplay.GameplayViewModel.GameplayState.FINISHED_GAME
import pt.isel.pdm.battleships.ui.screens.gameplay.gameplay.GameplayViewModel.GameplayState.GAME_LOADED
import pt.isel.pdm.battleships.ui.screens.gameplay.gameplay.GameplayViewModel.GameplayState.IDLE
import pt.isel.pdm.battleships.ui.screens.gameplay.gameplay.GameplayViewModel.GameplayState.LEAVING_GAME
import pt.isel.pdm.battleships.ui.screens.gameplay.gameplay.GameplayViewModel.GameplayState.LINKS_LOADED
import pt.isel.pdm.battleships.ui.screens.gameplay.gameplay.GameplayViewModel.GameplayState.LOADING_GAME
import pt.isel.pdm.battleships.ui.screens.gameplay.gameplay.GameplayViewModel.GameplayState.LOADING_MY_FLEET
import pt.isel.pdm.battleships.ui.screens.gameplay.gameplay.GameplayViewModel.GameplayState.MY_FLEET_LOADED
import pt.isel.pdm.battleships.ui.screens.gameplay.gameplay.GameplayViewModel.GameplayState.PLAYING_GAME
import pt.isel.pdm.battleships.ui.screens.shared.Event
import pt.isel.pdm.battleships.ui.screens.shared.executeRequestThrowing
import pt.isel.pdm.battleships.ui.screens.shared.launchAndExecuteRequest
import pt.isel.pdm.battleships.ui.screens.shared.launchAndExecuteRequestThrowing
import pt.isel.pdm.battleships.ui.screens.shared.navigation.Links

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
     * @property gameState the game state, null if the game is not loaded
     * @property myBoard the board of the player, null if the game is not loaded
     * @property opponentBoard the board of the opponent, null if the game is not loaded
     * @property myTurn true if it's the player's turn, false otherwise, null if the game is not loaded
     * @property player the name of the player, null if the game is not loaded or the user is not logged in
     * @property opponent  the name of the opponent, null if the game is not loaded or the user is not logged in
     * @property playerPoints the points of the player, null if the game is not loaded or the user is not logged in
     * @property opponentPoints the points of the opponent, null if the game is not loaded or the user is not logged in
     */
    data class GameplayScreenState(
        val gameConfig: GameConfig? = null,
        val gameState: GameState? = null,
        val myBoard: MyBoard? = null,
        val opponentBoard: OpponentBoard? = null,
        val myTurn: Boolean? = null,
        val player: Player? = null,
        val opponent: Player? = null,
        val playerPoints: Int? = null,
        val opponentPoints: Int? = null
    )

    private var _screenState by mutableStateOf(GameplayScreenState())
    private var _state: GameplayState by mutableStateOf(IDLE)

    val screenState: GameplayScreenState
        get() = _screenState

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

                val gameConfig = GameConfig(properties.config)
                val gameState = GameState(properties.state)

                val turn = gameState.turn ?: throw IllegalStateException("No turn found")
                val myTurn = turn == sessionManager.username

                val player = properties.players.single { it.username == sessionManager.username }
                val opponent = properties.players.single { it.username != sessionManager.username }

                _screenState = _screenState.copy(
                    gameConfig = gameConfig,
                    gameState = gameState,
                    opponentBoard = OpponentBoard(gameConfig.gridSize),
                    player = Player(player),
                    opponent = Player(opponent),
                    myTurn = myTurn
                )
                _state = GAME_LOADED

                getMyFleet()

                check(state == MY_FLEET_LOADED) {
                    "The view model is not in my fleet loaded state."
                }

                _state = PLAYING_GAME

                gameStatePolling()

                if (!myTurn) waitForOpponent()
            }
        )
    }

    /**
     * Gets the player's fleet.
     */
    private suspend fun getMyFleet() {
        check(state == GAME_LOADED) { "The view model is not in game loaded state." }

        _state = LOADING_MY_FLEET

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
        _state = MY_FLEET_LOADED
    }

    /**
     * Fires a list of shots.
     *
     * @param coordinates the list of coordinates where to fire
     */
    fun fireShots(coordinates: List<Coordinate>) {
        check(state == PLAYING_GAME) { "The game is not in the playing state" }
        check(_screenState.myTurn == true) { "It's not your turn" }

        launchAndExecuteRequest(
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

                if (state == FINISHED_GAME)
                    return@launchAndExecuteRequest

                _screenState = _screenState.copy(myTurn = false)

                waitForOpponent()
            },
            retryOnApiResultFailure = { problem ->
                // Check if failed because game ended by updating game state
                if (problem.status == 400)
                    viewModelScope.launch { updateGameState() }

                if (_state != FINISHED_GAME)
                    throw IllegalStateException(problem.title)

                false
            }
        )
    }

    /**
     * Waits for the opponent to play.
     */
    private suspend fun waitForOpponent() {
        check(state == PLAYING_GAME) { "The game is not in the playing state" }
        check(_screenState.myTurn == false) { "It's not the opponent's turn" }

        while (true) {
            if (_state == FINISHED_GAME)
                break

            if (_screenState.gameState?.turn != sessionManager.username)
                delay(POLLING_DELAY)
            else {
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
            ).shoot(opponentShots.map { it.coordinate.toCoordinate() })
        )
    }

    /**
     * Leaves the game.
     * Sends a [GameplayEvent.Exit] event when an api response is received, regardless of whether
     * or not it was successful.
     */
    fun leaveGame() {
        check(state == PLAYING_GAME) { "The game is not in the playing state" }
        _state = LEAVING_GAME

        launchAndExecuteRequest(
            request = { battleshipsService.gamesService.leaveGame() },
            events = _events,
            onSuccess = {
                _events.emit(GameplayEvent.Exit)
            },
            retryOnApiResultFailure = {
                _events.emit(GameplayEvent.Exit)
                false
            }
        )
    }

    /**
     * Gets the game state.
     * Updates the gameState property in [screenState] and the [state] property to
     * [GameplayState.FINISHED_GAME] in the case of the game being in [FINISHED_PHASE].
     */
    private suspend fun updateGameState() {
        val gameStateData = executeRequestThrowing(
            request = { battleshipsService.gamesService.getGameState() },
            events = _events
        )

        val gameState = GameState(
            gameStateModel = gameStateData.properties
                ?: throw IllegalStateException("Game state properties are null")
        )

        _screenState = _screenState.copy(gameState = gameState)

        if (gameState.phase == GamePhase.FINISHED) {
            launchAndExecuteRequestThrowing(
                request = { battleshipsService.gamesService.getGame() },
                events = _events,
                onSuccess = { getGameData ->
                    val properties = getGameData.properties
                        ?: throw IllegalStateException("No game properties found")

                    val player =
                        properties.players.single { it.username == sessionManager.username }
                    val opponent =
                        properties.players.single { it.username != sessionManager.username }

                    _screenState = _screenState.copy(
                        gameState = GameState(properties.state),
                        player = Player(player),
                        opponent = Player(opponent)
                    )

                    _state = FINISHED_GAME
                }
            )
        }
    }

    /**
     * Does the game state polling, calling updateGameState() every [POLLING_DELAY] milliseconds.
     * Finishes polling when state is [FINISHED_GAME].
     */
    private fun gameStatePolling() {
        viewModelScope.launch {
            while (true) {
                updateGameState()

                if (_state == FINISHED_GAME)
                    break

                delay(POLLING_DELAY)
            }
        }
    }

    /**
     * Parses a fleet into a list of ships.
     *
     * @param fleet the fleet to parse
     *
     * @return the list of ships
     * @throws IllegalStateException if the fleet is invalid
     */
    private fun parseFleet(fleet: GetFleetOutputModel, shipTypes: Map<ShipType, Int>): List<Ship> =
        fleet.ships.map {
            val shipType = shipTypes.keys
                .find { shipType -> shipType.shipName == it.type }
                ?: throw IllegalStateException("Invalid ship type")

            Ship(
                type = shipType,
                coordinate = it.coordinate.toCoordinate(),
                orientation = Orientation.valueOf(it.orientation)
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
     * @property IDLE the initial state
     * @property LINKS_LOADED the state when the links are loaded
     * @property LOADING_GAME the view model is loading the game
     * @property GAME_LOADED the game is loaded
     * @property LOADING_MY_FLEET the view model is loading the player's fleet
     * @property MY_FLEET_LOADED the player's fleet is loaded
     * @property PLAYING_GAME the game is being played
     * @property LEAVING_GAME the view model is leaving the game
     * @property FINISHED_GAME the game is finished
     */
    enum class GameplayState {
        IDLE,
        LINKS_LOADED,
        LOADING_GAME,
        GAME_LOADED,
        LOADING_MY_FLEET,
        MY_FLEET_LOADED,
        PLAYING_GAME,
        LEAVING_GAME,
        FINISHED_GAME
    }

    /**
     * The events that can be emitted.
     */
    sealed class GameplayEvent : Event {

        /**
         * The event of exiting the gameplay screen, going back to the gameplay menu screen.
         */
        object Exit : GameplayEvent()
    }

    companion object {
        private const val FINISHED_PHASE = "FINISHED"
        private const val POLLING_DELAY = 500L
        private const val TURN_SWITCH_DELAY = 1500L
    }
}
