package pt.isel.pdm.battleships.ui.screens.gameplay.boardSetup

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import pt.isel.pdm.battleships.SessionManager
import pt.isel.pdm.battleships.domain.games.ship.Ship
import pt.isel.pdm.battleships.services.BattleshipsService
import pt.isel.pdm.battleships.services.games.models.games.getGame.GetGameOutput
import pt.isel.pdm.battleships.services.games.models.players.deployFleet.DeployFleetInput
import pt.isel.pdm.battleships.services.utils.siren.EmbeddedLink
import pt.isel.pdm.battleships.ui.screens.gameplay.boardSetup.BoardSetupViewModel.BoardSetupState.DEPLOYING_FLEET
import pt.isel.pdm.battleships.ui.screens.gameplay.boardSetup.BoardSetupViewModel.BoardSetupState.GAME_LOADED
import pt.isel.pdm.battleships.ui.screens.gameplay.boardSetup.BoardSetupViewModel.BoardSetupState.LOADING_GAME
import pt.isel.pdm.battleships.ui.screens.gameplay.boardSetup.BoardSetupViewModel.BoardSetupState.WAITING_FOR_OPPONENT
import pt.isel.pdm.battleships.ui.utils.Event
import pt.isel.pdm.battleships.ui.utils.launchAndExecuteRequestRetrying
import pt.isel.pdm.battleships.ui.utils.navigation.Rels

/**
 * View model for the [BoardSetupActivity].
 *
 * @param battleshipsService the service of the battleships application
 * @property sessionManager the manager used to handle the user session
 *
 * @property state the current state of the view model
 * @property game the game being played
 * @property events the events that occurred in the view model
 */
class BoardSetupViewModel(
    battleshipsService: BattleshipsService,
    val sessionManager: SessionManager
) : ViewModel() {

    private val gamesService = battleshipsService.gamesService
    private val playersService = battleshipsService.playersService

    private var _state by mutableStateOf(LOADING_GAME)
    val state: BoardSetupState
        get() = _state

    private var _game by mutableStateOf<GetGameOutput?>(null)
    val game: GetGameOutput?
        get() = _game

    private val _events = MutableSharedFlow<Event>()
    val events: SharedFlow<Event> = _events

    /**
     * Loads the game.
     *
     * @param gameLink the link to the game
     */
    fun loadGame(gameLink: String) {
        check(state == BoardSetupState.IDLE) { "The game is not in the idle state" }

        _state = LOADING_GAME

        launchAndExecuteRequestRetrying(
            request = {
                val token = sessionManager.accessToken
                    ?: throw IllegalStateException("No token found")

                gamesService.getGame(token, gameLink)
            },
            events = _events,
            onSuccess = { getGameData ->
                _game = getGameData
                _state = GAME_LOADED
            }
        )
    }

    /**
     * Deploys the fleet.
     *
     * @param deployFleetLink the link to the deploy fleet endpoint
     * @param fleet the fleet to be deployed
     */
    fun deployFleet(deployFleetLink: String, fleet: List<Ship>) {
        check(state == GAME_LOADED) { "The game is not loaded" }

        _state = DEPLOYING_FLEET

        launchAndExecuteRequestRetrying(
            request = {
                playersService.deployFleet(
                    token = sessionManager.accessToken
                        ?: throw IllegalStateException("No token found"),
                    deployFleetLink = deployFleetLink,
                    fleet = DeployFleetInput(fleet = fleet.map(Ship::toUndeployedShipDTO))
                )
            },
            events = _events,
            onSuccess = {
                _state = BoardSetupState.FLEET_DEPLOYED
                waitForOpponent()
            }
        )
    }

    /**
     * Waits for the opponent to deploy their fleet.
     */
    private suspend fun waitForOpponent() {
        check(state == BoardSetupState.FLEET_DEPLOYED) {
            "The game is not in the fleet deployed state"
        }

        _state = WAITING_FOR_OPPONENT

        launchAndExecuteRequestRetrying(
            request = {
                gamesService.getGameState(
                    token = sessionManager.accessToken
                        ?: throw IllegalStateException("No token found"),
                    gameStateLink = game?.entities
                        ?.filterIsInstance<EmbeddedLink>()
                        ?.find { it.rel.contains(Rels.GAME_STATE) }?.href?.path
                        ?: throw IllegalStateException("Game state link not found")
                )
            },
            events = _events,
            onSuccess = { gameStateData ->
                val properties = gameStateData.properties
                    ?: throw IllegalStateException("Game state properties are null")

                if (properties.phase == "DEPLOYING_FLEETS") { // TODO: add constants
                    delay(1000L)
                } else {
                    _events.emit(BoardSetupEvent.NavigateToGameplay)
                    _state = BoardSetupState.FINISHED
                }
            }
        )
    }

    /**
     * The state of the view model.
     *
     * @property LOADING_GAME the view model is loading the game
     * @property DEPLOYING_FLEET the view model is deploying the fleet
     * @property WAITING_FOR_OPPONENT WAITING_FOR_OPPONENT
     */
    enum class BoardSetupState {
        IDLE,
        LOADING_GAME,
        GAME_LOADED,
        DEPLOYING_FLEET,
        FLEET_DEPLOYED,
        WAITING_FOR_OPPONENT,
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
    }
}
