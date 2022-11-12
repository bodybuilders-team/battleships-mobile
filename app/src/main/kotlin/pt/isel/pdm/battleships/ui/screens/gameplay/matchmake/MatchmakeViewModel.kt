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
import pt.isel.pdm.battleships.ui.utils.HTTPResult
import pt.isel.pdm.battleships.ui.utils.tryExecuteHttpRequest
import pt.isel.pdm.battleships.utils.Rels.GAME
import pt.isel.pdm.battleships.utils.Rels.MATCHMAKE_STATE

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
 */
class MatchmakeViewModel(
    private val gamesService: GamesService,
    private val sessionManager: SessionManager,
    jsonEncoder: Gson,
    assetManager: AssetManager
) : ViewModel() {

    var state by mutableStateOf(MatchmakeState.IDLE)
    var gameLink: String? by mutableStateOf(null)

    // TODO: change this
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

        state = MatchmakeState.MATCHMAKING

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
                        state = MatchmakeState.MATCHMAKING
                        continue
                    }
                }

                gameStateLink = when (res) {
                    is APIResult.Success -> {
                        val properties = res.data.properties
                            ?: throw IllegalStateException("Game properties are null")

                        val entities = res.data.entities
                            ?: throw IllegalStateException("Game entities are null")

                        val matchGameLink = res.data.entities
                            .filterIsInstance<EmbeddedLink>()
                            .first { it.rel.contains(GAME) }.href.path

                        val matchGameStateLink = entities
                            .filterIsInstance<EmbeddedLink>()
                            .first { it.rel.contains(MATCHMAKE_STATE) }.href.path

                        gameLink = matchGameLink
                        if (!properties.wasCreated) {
                            _events.emit(MatchmakeEvent.NavigateToBoardSetup)
                            state = MatchmakeState.MATCHMADE
                            return@launch
                        }

                        createdGame = true
                        matchGameStateLink
                    }
                    is APIResult.Failure -> {
                        _events.emit(MatchmakeEvent.Error(res.error.title))
                        state = MatchmakeState.MATCHMAKING
                        continue
                    }
                }
            }

            while (state != MatchmakeState.MATCHMADE) {
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
                        state = MatchmakeState.MATCHMAKING
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
                            state = MatchmakeState.MATCHMADE
                        }
                    }
                    is APIResult.Failure -> {
                        _events.emit(MatchmakeEvent.Error(gameStateRes.error.title))
                        state = MatchmakeState.MATCHMAKING
                        continue
                    }
                }
            }
        }
    }

    /**
     * Represents the quick play operation state.
     *
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
        class Error(val message: String) : MatchmakeEvent()
        object NavigateToBoardSetup : MatchmakeEvent()
    }

    companion object {
        private const val DEFAULT_GAME_CONFIG_FILE_PATH = "defaultGameConfig.json"
        private const val POLLING_DELAY = 500L
        private const val ANIMATION_DELAY = 1500L
    }
}
