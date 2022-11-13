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
import pt.isel.pdm.battleships.services.games.dtos.GameConfigDTO
import pt.isel.pdm.battleships.services.utils.APIResult
import pt.isel.pdm.battleships.services.utils.siren.EmbeddedLink
import pt.isel.pdm.battleships.ui.screens.gameplay.matchmake.MatchmakeViewModel.MatchmakeState.IDLE
import pt.isel.pdm.battleships.ui.screens.gameplay.matchmake.MatchmakeViewModel.MatchmakeState.MATCHMADE
import pt.isel.pdm.battleships.ui.screens.gameplay.matchmake.MatchmakeViewModel.MatchmakeState.MATCHMAKING
import pt.isel.pdm.battleships.ui.utils.HTTPResult
import pt.isel.pdm.battleships.ui.utils.navigation.Rels.GAME
import pt.isel.pdm.battleships.ui.utils.navigation.Rels.GAME_STATE
import pt.isel.pdm.battleships.ui.utils.tryExecuteHttpRequest

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

    var state by mutableStateOf(MatchmakeState.IDLE)
    var gameLink: String? by mutableStateOf(null)

    private val gameConfigDTO = jsonEncoder.fromJson<GameConfigDTO>(
        JsonReader(assetManager.open(DEFAULT_GAME_CONFIG_FILE_PATH).reader()),
        GameConfigDTO::class.java
    )

    private val _events = MutableSharedFlow<MatchmakeEvent>()
    val events: SharedFlow<MatchmakeEvent> = _events

    /**
     * Matchmakes a game with the default configuration.
     *
     * @param matchmakeLink the link to the matchmake endpoint
     */
    fun matchmake(matchmakeLink: String) {
        if (state != MatchmakeState.IDLE) return

        state = MATCHMAKING

        viewModelScope.launch {
            delay(ANIMATION_DELAY)

            val token = sessionManager.accessToken ?: throw IllegalStateException("No token found")

            var createdGame = false
            var gameStateLink: String? = null

            while (!createdGame) {
                val httpRes = tryExecuteHttpRequest {
                    gamesService.matchmake(token, matchmakeLink, gameConfigDTO)
                }

                val res = when (httpRes) {
                    is HTTPResult.Success -> httpRes.data
                    is HTTPResult.Failure -> {
                        _events.emit(MatchmakeEvent.Error(httpRes.error))
                        state = MATCHMAKING
                        continue
                    }
                }

                when (res) {
                    is APIResult.Success -> {
                        val properties = res.data.properties
                            ?: throw IllegalStateException("Game properties are null")

                        val entities = res.data.entities
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
                        if (!properties.wasCreated) {
                            _events.emit(MatchmakeEvent.NavigateToBoardSetup)
                            state = MATCHMADE
                            return@launch
                        }

                        createdGame = true
                        gameStateLink = matchGameStateLink
                    }
                    is APIResult.Failure -> {
                        _events.emit(MatchmakeEvent.Error(res.error.title))
                        state = MATCHMAKING
                        continue
                    }
                }
            }

            while (state != MATCHMADE) {
                val gameStateHttpRes = tryExecuteHttpRequest {
                    gamesService.getGameState(
                        token,
                        gameStateLink ?: throw IllegalStateException("Game state link is null")
                    )
                }

                val gameStateRes = when (gameStateHttpRes) {
                    is HTTPResult.Success -> gameStateHttpRes.data
                    is HTTPResult.Failure -> {
                        _events.emit(MatchmakeEvent.Error(gameStateHttpRes.error))
                        state = MATCHMAKING
                        continue
                    }
                }

                when (gameStateRes) {
                    is APIResult.Success -> {
                        val properties = gameStateRes.data.properties
                            ?: throw IllegalStateException("Game state properties are null")

                        if (properties.phase == "WAITING_FOR_PLAYERS") { // TODO: add constants
                            delay(POLLING_DELAY)
                        } else {
                            _events.emit(MatchmakeEvent.NavigateToBoardSetup)
                            state = MATCHMADE
                        }
                    }
                    is APIResult.Failure -> {
                        _events.emit(MatchmakeEvent.Error(gameStateRes.error.title))
                        state = MATCHMAKING
                        continue
                    }
                }
            }
        }
    }

    /**
     * Represents the quick play operation state.
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
     * Represents the events that can be emitted.
     */
    sealed class MatchmakeEvent {

        /**
         * Represents an error event.
         *
         * @property message the error message
         */
        class Error(val message: String) : MatchmakeEvent()

        /**
         * Navigation event to the board setup screen.
         */
        object NavigateToBoardSetup : MatchmakeEvent()
    }

    companion object {
        private const val DEFAULT_GAME_CONFIG_FILE_PATH = "defaultGameConfig.json"
        private const val POLLING_DELAY = 500L
        private const val ANIMATION_DELAY = 1500L
    }
}
