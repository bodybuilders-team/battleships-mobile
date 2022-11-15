package pt.isel.pdm.battleships.ui.screens.gameplay.matchmake

import android.content.res.AssetManager
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import pt.isel.pdm.battleships.SessionManager
import pt.isel.pdm.battleships.services.games.GamesService
import pt.isel.pdm.battleships.services.games.models.games.GameConfigModel
import pt.isel.pdm.battleships.services.utils.siren.EmbeddedLink
import pt.isel.pdm.battleships.ui.screens.gameplay.matchmake.MatchmakeViewModel.MatchmakeState.IDLE
import pt.isel.pdm.battleships.ui.screens.gameplay.matchmake.MatchmakeViewModel.MatchmakeState.MATCHMADE
import pt.isel.pdm.battleships.ui.screens.gameplay.matchmake.MatchmakeViewModel.MatchmakeState.MATCHMAKING
import pt.isel.pdm.battleships.ui.utils.Event
import pt.isel.pdm.battleships.ui.utils.executeRequestRetrying
import pt.isel.pdm.battleships.ui.utils.navigation.Rels.GAME
import pt.isel.pdm.battleships.ui.utils.navigation.Rels.GAME_STATE

/**
 * View model for the [MatchmakeActivity].
 *
 * @property gamesService the service that handles the games
 * @property sessionManager the manager used to handle the user session
 * @param jsonEncoder the JSON formatter
 * @param assetManager the asset manager
 *
 * @property state the current state of the view model
 * @property gameLink the id of the game that was created
 * @property events the events that can be emitted by the view model
 */
class MatchmakeViewModel(
    private val gamesService: GamesService,
    private val sessionManager: SessionManager,
    jsonEncoder: Gson,
    assetManager: AssetManager
) : ViewModel() {

    var state by mutableStateOf(IDLE)
    var gameLink: String? by mutableStateOf(null)

    private val gameConfigModel = jsonEncoder.fromJson<GameConfigModel>(
        JsonReader(assetManager.open(DEFAULT_GAME_CONFIG_FILE_PATH).reader()),
        GameConfigModel::class.java
    )

    private val _events = MutableSharedFlow<Event>()
    val events: SharedFlow<Event> = _events

    /**
     * Matchmakes a game with the default configuration.
     *
     * @param matchmakeLink the link to the matchmake endpoint
     */
    fun matchmake(matchmakeLink: String) {
        check(state == IDLE) { "The view model is not in the idle state" }

        state = MATCHMAKING

        viewModelScope.launch {
            delay(ANIMATION_DELAY)

            val token = sessionManager.accessToken
                ?: throw IllegalStateException("No token found")

            var gameStateLink: String? = null

            val matchmakeData = executeRequestRetrying(
                request = {
                    gamesService.matchmake(
                        token = token,
                        matchmakeLink = matchmakeLink,
                        gameConfig = gameConfigModel
                    )
                },
                events = _events
            )

            val properties = matchmakeData.properties
                ?: throw IllegalStateException("Game properties are null")

            val entities = matchmakeData.entities
                ?: throw IllegalStateException("Game entities are null")

            val matchGameLink = entities
                .filterIsInstance<EmbeddedLink>()
                .find { it.rel.contains(GAME) }?.href?.path
                ?: throw IllegalStateException("Game link not found")

            val matchGameStateLink = entities
                .filterIsInstance<EmbeddedLink>()
                .find { it.rel.contains(GAME_STATE) }?.href?.path
                ?: throw IllegalStateException("Game state link not found")

            gameLink = matchGameLink
            if (properties.wasCreated) {
                gameStateLink = matchGameStateLink
            } else {
                _events.emit(MatchmakeEvent.NavigateToBoardSetup)
                state = MATCHMADE
            }

            val gameStateData = executeRequestRetrying(
                request = {
                    gamesService.getGameState(
                        token = token,
                        gameStateLink = gameStateLink
                            ?: throw IllegalStateException("Game state link is null")
                    )
                },
                events = _events
            )

            val gameStateProperties = gameStateData.properties
                ?: throw IllegalStateException("Game state properties are null")

            if (gameStateProperties.phase == WAITING_FOR_PLAYERS_PHASE) {
                delay(POLLING_DELAY)
            } else {
                _events.emit(MatchmakeEvent.NavigateToBoardSetup)
                state = MATCHMADE
            }
        }
    }

    /**
     * The matchmake operation state.
     *
     * @property IDLE the idle state
     * @property MATCHMAKING the matchmaking is in progress
     * @property MATCHMADE the matchmake was successful
     */
    enum class MatchmakeState {
        IDLE,
        MATCHMAKING,
        MATCHMADE
    }

    /**
     * The events that can be emitted.
     */
    sealed class MatchmakeEvent : Event {

        /**
         * Navigation event to the board setup screen.
         */
        object NavigateToBoardSetup : MatchmakeEvent()
    }

    companion object {
        private const val DEFAULT_GAME_CONFIG_FILE_PATH = "defaultGameConfig.json"
        private const val WAITING_FOR_PLAYERS_PHASE = "WAITING_FOR_PLAYERS"
        private const val POLLING_DELAY = 500L
        private const val ANIMATION_DELAY = 1500L
    }
}
