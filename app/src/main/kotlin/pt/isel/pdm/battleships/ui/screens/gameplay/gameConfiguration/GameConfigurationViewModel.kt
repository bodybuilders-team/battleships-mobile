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
import pt.isel.pdm.battleships.services.utils.siren.EmbeddedLink
import pt.isel.pdm.battleships.ui.screens.gameplay.gameConfiguration.GameConfigurationViewModel.GameConfigurationState.CREATING_GAME
import pt.isel.pdm.battleships.ui.screens.gameplay.gameConfiguration.GameConfigurationViewModel.GameConfigurationState.GAME_CREATED
import pt.isel.pdm.battleships.ui.screens.gameplay.gameConfiguration.GameConfigurationViewModel.GameConfigurationState.IDLE
import pt.isel.pdm.battleships.ui.utils.Event
import pt.isel.pdm.battleships.ui.utils.handle
import pt.isel.pdm.battleships.ui.utils.navigation.Rels
import pt.isel.pdm.battleships.ui.utils.tryExecuteHttpRequest

/**
 * View model for the [GameConfigurationActivity].
 *
 * @property gamesService the service that handles the games
 *
 * @property state the current state of the view model
 * @property gameLink the link to the game
 * @property events the events that can be emitted by the view model
 */
class GameConfigurationViewModel(
    private val gamesService: GamesService,
    private val sessionManager: SessionManager
) : ViewModel() {

    var state by mutableStateOf(IDLE)
    var gameLink: String? by mutableStateOf(null)

    private val _events = MutableSharedFlow<Event>()
    val events: SharedFlow<Event> = _events

    /**
     * Creates a new game.
     *
     * @param createGameLink the link to the create game endpoint
     * @param gameConfig the game configuration
     */
    fun createGame(createGameLink: String, gameConfig: GameConfig) {
        check(state == IDLE) { "The view model is not in the idle state" }

        state = CREATING_GAME

        viewModelScope.launch {
            while (state == CREATING_GAME) {
                val httpRes = tryExecuteHttpRequest {
                    gamesService.createGame(
                        token = sessionManager.accessToken
                            ?: throw IllegalStateException("The user is not logged in"),
                        createGameLink = createGameLink,
                        gameConfig = gameConfig.toGameConfigDTO()
                    )
                }

                val res = httpRes.handle(events = _events) ?: return@launch

                res.handle(
                    events = _events,
                    onSuccess = { createGameData ->
                        val entities = createGameData.entities
                            ?: throw IllegalStateException("No entities in response")

                        gameLink = entities
                            .filterIsInstance<EmbeddedLink>()
                            .first { it.rel.contains(Rels.GAME) }.href.path

                        state = GAME_CREATED
                        _events.emit(GameConfigurationEvent.NavigateToBoardSetup)
                    }
                )
            }
        }
    }

    /**
     * The game configuration state.
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
     * The events that can be emitted.
     */
    sealed class GameConfigurationEvent : Event {

        /**
         * A navigation event to the board setup screen.
         */
        object NavigateToBoardSetup : GameConfigurationEvent()
    }
}
