package pt.isel.pdm.battleships.ui.screens.gameplay.gameConfiguration

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import pt.isel.pdm.battleships.SessionManager
import pt.isel.pdm.battleships.domain.games.game.GameConfig
import pt.isel.pdm.battleships.services.games.GamesService
import pt.isel.pdm.battleships.services.utils.APIResult
import pt.isel.pdm.battleships.services.utils.siren.EmbeddedLink
import pt.isel.pdm.battleships.ui.screens.gameplay.gameConfiguration.GameConfigurationViewModel.GameConfigurationState.CREATING_GAME
import pt.isel.pdm.battleships.ui.screens.gameplay.gameConfiguration.GameConfigurationViewModel.GameConfigurationState.GAME_CREATED
import pt.isel.pdm.battleships.ui.screens.gameplay.gameConfiguration.GameConfigurationViewModel.GameConfigurationState.IDLE
import pt.isel.pdm.battleships.ui.utils.HTTPResult
import pt.isel.pdm.battleships.ui.utils.tryExecuteHttpRequest

/**
 * View model for the [GameConfigurationActivity].
 *
 * @property gamesService the service that handles the games
 *
 * @property state the current state of the view model
 * @property gameLink the link to the game
 * @property errorMessage the error message to be displayed
 * @property events the events that can be emitted by the view model
 */
class GameConfigurationViewModel(
    private val gamesService: GamesService,
    private val sessionManager: SessionManager
) : ViewModel() {

    var state by mutableStateOf(IDLE)
    var gameLink: String? by mutableStateOf(null)
    var errorMessage: String? by mutableStateOf(null)

    private val _events = MutableSharedFlow<GameConfigurationEvent>()
    val events: SharedFlow<GameConfigurationEvent> = _events

    /**
     * Creates a new game.
     *
     * @param createGameLink the link to the create game endpoint
     * @param gameConfig the game configuration
     */
    fun createGame(createGameLink: String, gameConfig: GameConfig) {
        state = CREATING_GAME

        viewModelScope.launch {
            val httpRes = tryExecuteHttpRequest {
                gamesService.createGame(
                    sessionManager.accessToken!!,
                    createGameLink,
                    gameConfig.toGameConfigDTO()
                )
            }

            val res = when (httpRes) {
                is HTTPResult.Success -> httpRes.data
                is HTTPResult.Failure -> {
                    _events.emit(GameConfigurationEvent.Error(httpRes.error))
                    state = IDLE
                    return@launch
                }
            }

            when (res) {
                is APIResult.Success -> {
                    val entities = res.data.entities
                        ?: throw IllegalStateException("No entities in response")

                    gameLink = entities
                        .filterIsInstance<EmbeddedLink>()
                        .first { it.rel.contains("game") }.href.path

                    state = GAME_CREATED
                    _events.emit(GameConfigurationEvent.NavigateToBoardSetup)
                }
                is APIResult.Failure -> {
                    _events.emit(GameConfigurationEvent.Error(res.error.title))
                    state = CREATING_GAME
                    return@launch
                }
            }
        }
    }

    /**
     * Represents the game configuration state.
     *
     * @property IDLE the game configuration operation is idle
     * @property CREATING_GAME creating a new game
     * @property GAME_CREATED the game was created
     */
    enum class GameConfigurationState {
        IDLE,
        CREATING_GAME,
        GAME_CREATED
    }

    /**
     * Represents the events that can be emitted.
     */
    sealed class GameConfigurationEvent {

        /**
         * Represents an error event.
         *
         * @property message the error message
         */
        class Error(val message: String) : GameConfigurationEvent()

        /**
         * Represents a navigation event to the board setup screen.
         */
        object NavigateToBoardSetup : GameConfigurationEvent()
    }
}
