package pt.isel.pdm.battleships.ui.screens.gameplay.boardSetup

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import pt.isel.pdm.battleships.SessionManager
import pt.isel.pdm.battleships.domain.games.ship.Ship
import pt.isel.pdm.battleships.services.BattleshipsService
import pt.isel.pdm.battleships.services.games.dtos.GameDTO
import pt.isel.pdm.battleships.services.games.dtos.ship.UndeployedFleetDTO
import pt.isel.pdm.battleships.services.utils.APIResult
import pt.isel.pdm.battleships.services.utils.siren.EmbeddedLink
import pt.isel.pdm.battleships.ui.screens.gameplay.boardSetup.BoardSetupViewModel.BoardSetupState.DEPLOYING_FLEET
import pt.isel.pdm.battleships.ui.screens.gameplay.boardSetup.BoardSetupViewModel.BoardSetupState.FLEET_DEPLOYED
import pt.isel.pdm.battleships.ui.screens.gameplay.boardSetup.BoardSetupViewModel.BoardSetupState.LOADING_GAME
import pt.isel.pdm.battleships.ui.utils.HTTPResult
import pt.isel.pdm.battleships.ui.utils.navigation.Rels
import pt.isel.pdm.battleships.ui.utils.tryExecuteHttpRequest

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

    var state by mutableStateOf(LOADING_GAME)
    var game by mutableStateOf<GameDTO?>(null)

    private val _events = MutableSharedFlow<BoardSetupEvent>()
    val events: SharedFlow<BoardSetupEvent> = _events

    /**
     * Loads the game.
     *
     * @param gameLink the link to the game
     */
    fun loadGame(gameLink: String) {
        if (state != LOADING_GAME) return

        viewModelScope.launch {
            val token = sessionManager.accessToken ?: throw IllegalStateException("No token found")
            val httpRes = tryExecuteHttpRequest {
                gamesService.getGame(token, gameLink)
            }

            val res = when (httpRes) {
                is HTTPResult.Success -> {
                    httpRes.data
                }
                is HTTPResult.Failure -> {
                    _events.emit(BoardSetupEvent.Error(httpRes.error))
                    state = LOADING_GAME
                    return@launch
                }
            }

            when (res) {
                is APIResult.Success -> {
                    game = res.data
                    state = DEPLOYING_FLEET
                }
                is APIResult.Failure -> {
                    _events.emit(BoardSetupEvent.Error(res.error.title))
                    state = LOADING_GAME
                    return@launch
                }
            }
        }
    }

    /**
     * Deploys the fleet.
     *
     * @param deployFleetLink the link to the deploy fleet endpoint
     * @param fleet the fleet to be deployed
     */
    fun deployFleet(deployFleetLink: String, fleet: List<Ship>) {
        if (state != DEPLOYING_FLEET) return

        viewModelScope.launch {
            val token = sessionManager.accessToken ?: throw IllegalStateException("No token found")
            val fleetDTOs = fleet.map(Ship::toUndeployedShipDTO)
            Log.v("BoardSetupLog", "Deploying fleet: $fleetDTOs")

            val httpRes = tryExecuteHttpRequest {
                playersService.deployFleet(
                    token = token,
                    deployLink = deployFleetLink,
                    fleet = UndeployedFleetDTO(fleetDTOs)
                )
            }

            val res = when (httpRes) {
                is HTTPResult.Success -> {
                    httpRes.data
                }
                is HTTPResult.Failure -> {
                    _events.emit(BoardSetupEvent.Error(httpRes.error))
                    state = DEPLOYING_FLEET
                    return@launch
                }
            }

            when (res) {
                is APIResult.Success -> {
                    state = FLEET_DEPLOYED

                    val gameStateLink = game?.entities
                        ?.filterIsInstance<EmbeddedLink>()
                        ?.find { it.rel.contains(Rels.GAME_STATE) }?.href?.path
                        ?: throw IllegalStateException("Game state link not found")

                    while (true) {
                        val gameStateHttpRes = tryExecuteHttpRequest {
                            gamesService.getGameState(
                                token,
                                gameStateLink
                            )
                        }

                        val gameStateRes = when (gameStateHttpRes) {
                            is HTTPResult.Success -> gameStateHttpRes.data
                            is HTTPResult.Failure -> {
                                _events.emit(
                                    BoardSetupEvent.Error(
                                        gameStateHttpRes.error
                                    )
                                )
                                state = FLEET_DEPLOYED
                                continue
                            }
                        }

                        when (gameStateRes) {
                            is APIResult.Success -> {
                                val properties = gameStateRes.data.properties
                                    ?: throw IllegalStateException("Game state properties are null")

                                if (properties.phase == "DEPLOYING_FLEETS") { // TODO: add constants
                                    delay(1000L)
                                } else {
                                    _events.emit(BoardSetupEvent.NavigateToGameplay)
                                    state = FLEET_DEPLOYED
                                    break
                                }
                            }
                            is APIResult.Failure -> {
                                _events.emit(BoardSetupEvent.Error(gameStateRes.error.title))
                                state = FLEET_DEPLOYED
                                continue
                            }
                        }
                    }
                }
                is APIResult.Failure -> {
                    _events.emit(BoardSetupEvent.Error(res.error.title))
                    state = DEPLOYING_FLEET
                    return@launch
                }
            }
        }
    }

    /**
     * The state of the view model.
     *
     * @property LOADING_GAME the view model is loading the game
     * @property DEPLOYING_FLEET the view model is deploying the fleet
     * @property FLEET_DEPLOYED the fleet has been deployed
     */
    enum class BoardSetupState {
        LOADING_GAME,
        DEPLOYING_FLEET,
        FLEET_DEPLOYED
    }

    /**
     * Represents the events that can be emitted.
     */
    sealed class BoardSetupEvent {

        /**
         * Represents an error that occurred.
         *
         * @property message the message of the error
         */
        class Error(val message: String) : BoardSetupEvent()

        /**
         * Represents the event of navigating to the gameplay screen.
         */
        object NavigateToGameplay : BoardSetupEvent()
    }
}
