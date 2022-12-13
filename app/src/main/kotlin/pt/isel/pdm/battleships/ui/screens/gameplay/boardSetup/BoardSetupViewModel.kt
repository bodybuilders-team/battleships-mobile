package pt.isel.pdm.battleships.ui.screens.gameplay.boardSetup

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import pt.isel.pdm.battleships.domain.games.game.GamePhase
import pt.isel.pdm.battleships.domain.games.game.GameState
import pt.isel.pdm.battleships.domain.games.ship.Ship
import pt.isel.pdm.battleships.domain.games.ship.ShipType
import pt.isel.pdm.battleships.domain.users.Player
import pt.isel.pdm.battleships.service.BattleshipsService
import pt.isel.pdm.battleships.service.services.games.models.players.deployFleet.DeployFleetInput
import pt.isel.pdm.battleships.session.SessionManager
import pt.isel.pdm.battleships.ui.screens.BattleshipsViewModel
import pt.isel.pdm.battleships.ui.screens.gameplay.boardSetup.BoardSetupViewModel.BoardSetupState.DEPLOYING_FLEET
import pt.isel.pdm.battleships.ui.screens.gameplay.boardSetup.BoardSetupViewModel.BoardSetupState.FINISHED
import pt.isel.pdm.battleships.ui.screens.gameplay.boardSetup.BoardSetupViewModel.BoardSetupState.FLEET_DEPLOYED
import pt.isel.pdm.battleships.ui.screens.gameplay.boardSetup.BoardSetupViewModel.BoardSetupState.GAME_LOADED
import pt.isel.pdm.battleships.ui.screens.gameplay.boardSetup.BoardSetupViewModel.BoardSetupState.IDLE
import pt.isel.pdm.battleships.ui.screens.gameplay.boardSetup.BoardSetupViewModel.BoardSetupState.LEAVING_GAME
import pt.isel.pdm.battleships.ui.screens.gameplay.boardSetup.BoardSetupViewModel.BoardSetupState.LINKS_LOADED
import pt.isel.pdm.battleships.ui.screens.gameplay.boardSetup.BoardSetupViewModel.BoardSetupState.LOADING_GAME
import pt.isel.pdm.battleships.ui.screens.gameplay.boardSetup.BoardSetupViewModel.BoardSetupState.WAITING_FOR_OPPONENT
import pt.isel.pdm.battleships.ui.screens.shared.Event
import pt.isel.pdm.battleships.ui.screens.shared.executeRequestThrowing
import pt.isel.pdm.battleships.ui.screens.shared.launchAndExecuteRequest
import pt.isel.pdm.battleships.ui.screens.shared.launchAndExecuteRequestThrowing
import pt.isel.pdm.battleships.ui.screens.shared.navigation.Links

/**
 * View model for the [BoardSetupActivity].
 *
 * @param battleshipsService the service used to handle the battleships game
 * @property sessionManager the manager used to handle the user session
 *
 * @property events the events that occurred in the view model
 */
class BoardSetupViewModel(
    battleshipsService: BattleshipsService,
    sessionManager: SessionManager
) : BattleshipsViewModel(battleshipsService, sessionManager) {

    /**
     * The state of the board setup screen.
     *
     * @property gridSize the size of the grid
     * @property ships the ships that are available to be deployed
     */
    data class BoardSetupScreenState(
        val gridSize: Int? = null,
        val ships: Map<ShipType, Int>? = null,
        val gameState: GameState? = null,
        val player: Player? = null,
        val opponent: Player? = null
    )

    private var _screenState by mutableStateOf(BoardSetupScreenState())
    private var _state: BoardSetupState by mutableStateOf(IDLE)

    val screenState: BoardSetupScreenState
        get() = _screenState

    val state
        get() = _state

    /**
     * Loads the game.
     */
    fun loadGame() {
        check(state == LINKS_LOADED) { "The game is not in the links loaded state" }

        _state = LOADING_GAME

        launchAndExecuteRequestThrowing(
            request = { battleshipsService.gamesService.getGame() },
            events = _events,
            onSuccess = { getGameData ->
                val properties = getGameData.properties
                    ?: throw IllegalStateException("No game properties found")

                val shipTypes = properties.config.shipTypes

                val player = properties.players.single { it.username == sessionManager.username }
                val opponent = properties.players.single { it.username != sessionManager.username }

                _screenState = _screenState.copy(
                    gridSize = properties.config.gridSize,
                    ships = shipTypes.associate {
                        ShipType(size = it.size, shipName = it.shipName) to it.quantity
                    },
                    gameState = GameState(properties.state),
                    player = Player(player),
                    opponent = Player(opponent)
                )
                _state = GAME_LOADED

                viewModelScope.launch { gameStatePolling() }
            }
        )
    }

    /**
     * Deploys the fleet.
     *
     * @param fleet the fleet to be deployed
     */
    fun deployFleet(fleet: List<Ship>) {
        check(state == GAME_LOADED) { "The game is not loaded" }

        _state = DEPLOYING_FLEET

        launchAndExecuteRequestThrowing(
            request = {
                battleshipsService.playersService.deployFleet(
                    fleet = DeployFleetInput(fleet = fleet.map(Ship::toUndeployedShipModel))
                )
            },
            events = _events,
            onSuccess = {
                _state = FLEET_DEPLOYED
                waitForOpponent()
            }
        )
    }

    /**
     * Leaves the game.
     * Sends a [BoardSetupEvent.Exit] event when an api response is received, regardless of whether
     * or not it was successful.
     */
    fun leaveGame() {
        check(state == GAME_LOADED) { "The game is not loaded" }
        _state = LEAVING_GAME

        launchAndExecuteRequest(
            request = { battleshipsService.gamesService.leaveGame() },
            events = _events,
            onSuccess = {
                _events.emit(BoardSetupEvent.Exit)
            },
            retryOnApiResultFailure = {
                _events.emit(BoardSetupEvent.Exit)
                false
            }
        )
    }

    /**
     * Waits for the opponent to deploy their fleet.
     */
    private suspend fun waitForOpponent() {
        check(state == FLEET_DEPLOYED) { "The game is not in the fleet deployed state" }

        _state = WAITING_FOR_OPPONENT

        while (true) {
            val gameStateData = executeRequestThrowing(
                request = { battleshipsService.gamesService.getGameState() },
                events = _events
            )

            val properties = GameState(
                gameStateData.properties
                    ?: throw IllegalStateException("Game state properties are null")
            )

            if (properties.phase == GamePhase.DEPLOYING_FLEETS)
                delay(POLLING_DELAY)
            else {
                _state = FINISHED
                _events.emit(BoardSetupEvent.NavigateToGameplay)
                break
            }
        }
    }

    /**
     * Gets the game state.
     * Updates the gameState property in [screenState] and the [state] property to
     * [BoardSetupState.FINISHED] in the case of the game being in [GamePhase.FINISHED].
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

                    val shipTypes = properties.config.shipTypes

                    val player =
                        properties.players.single { it.username == sessionManager.username }
                    val opponent =
                        properties.players.single { it.username != sessionManager.username }

                    _screenState = _screenState.copy(
                        gridSize = properties.config.gridSize,
                        ships = shipTypes.associate {
                            ShipType(size = it.size, shipName = it.shipName) to it.quantity
                        },
                        gameState = GameState(properties.state),
                        player = Player(player),
                        opponent = Player(opponent)
                    )

                    _state = FINISHED
                }
            )
        }
    }

    /**
     * Does the game state polling, calling updateGameState() every [POLLING_DELAY] milliseconds.
     * Finishes polling when state is [FINISHED].
     */
    private fun gameStatePolling() {
        viewModelScope.launch {
            while (true) {
                updateGameState()

                if (_state == FINISHED)
                    break

                delay(POLLING_DELAY)
            }
        }
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
     * @property DEPLOYING_FLEET the view model is deploying the fleet
     * @property FLEET_DEPLOYED the fleet is deployed
     * @property WAITING_FOR_OPPONENT the view model is waiting for the opponent to deploy their fleet
     * @property LEAVING_GAME the view model is leaving the game
     * @property FINISHED the board setup is finished
     */
    enum class BoardSetupState {
        IDLE,
        LINKS_LOADED,
        LOADING_GAME,
        GAME_LOADED,
        DEPLOYING_FLEET,
        FLEET_DEPLOYED,
        WAITING_FOR_OPPONENT,
        LEAVING_GAME,
        FINISHED
    }

    /**
     * The events that can be emitted.
     */
    sealed class BoardSetupEvent : Event {

        /**
         * The event of navigating to the gameplay screen.
         */
        object NavigateToGameplay : BoardSetupEvent()

        /**
         * The event of exiting the board setup screen, going back to the gameplay menu screen.
         */
        object Exit : BoardSetupEvent()
    }

    companion object {
        private const val POLLING_DELAY = 500L
    }
}
