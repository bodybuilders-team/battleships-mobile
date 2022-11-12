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
import kotlinx.coroutines.launch
import pt.isel.pdm.battleships.SessionManager
import pt.isel.pdm.battleships.services.games.GamesService
import pt.isel.pdm.battleships.services.games.dtos.GameConfigDTO
import pt.isel.pdm.battleships.services.utils.APIResult
import pt.isel.pdm.battleships.services.utils.siren.EmbeddedLink
import pt.isel.pdm.battleships.ui.screens.gameplay.matchmake.MatchmakeViewModel.MatchmakeState.ERROR
import pt.isel.pdm.battleships.ui.screens.gameplay.matchmake.MatchmakeViewModel.MatchmakeState.MATCHMADE
import pt.isel.pdm.battleships.ui.screens.gameplay.matchmake.MatchmakeViewModel.MatchmakeState.MATCHMAKING
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
 * @property errorMessage the error message to be displayed
 */
class MatchmakeViewModel(
    private val gamesService: GamesService,
    private val sessionManager: SessionManager,
    jsonEncoder: Gson,
    assetManager: AssetManager
) : ViewModel() {

    var state by mutableStateOf(MatchmakeState.IDLE)
    var gameLink: String? by mutableStateOf(null)
    var errorMessage: String? by mutableStateOf(null)

    // TODO: change this
    val gameConfigDTO = jsonEncoder.fromJson<GameConfigDTO>(
        JsonReader(assetManager.open(DEFAULT_GAME_CONFIG_FILE_PATH).reader()),
        GameConfigDTO::class.java
    )

    /**
     * Matchmakes a game with the default configuration.
     *
     * @param matchmakeLink the link to the matchmake endpoint
     */
    fun matchmake(matchmakeLink: String) {
        if (state != MatchmakeState.IDLE) return

        viewModelScope.launch {
            delay(ANIMATION_DELAY)

            val token = sessionManager.accessToken ?: throw IllegalStateException("No token found")

            val gameStateLink = when (
                val res = gamesService.matchmake(token, matchmakeLink, gameConfigDTO)
            ) {
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
                        state = MATCHMADE
                        return@launch
                    }

                    matchGameStateLink
                }
                is APIResult.Failure -> {
                    errorMessage = res.error.title
                    state = ERROR
                    return@launch
                }
            }

            while (state != MATCHMADE) {
                when (val res = gamesService.getGameState(token, gameStateLink)) {
                    is APIResult.Success -> {
                        val properties = res.data.properties
                            ?: throw IllegalStateException("Game state properties are null")

                        if (properties.phase == "WAITING_FOR_PLAYERS") { // TODO: add constants
                            delay(POLLING_DELAY)
                        } else {
                            state = MATCHMADE
                        }
                    }
                    is APIResult.Failure -> {
                        errorMessage = res.error.title
                        state = ERROR
                        return@launch
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
     * @property ERROR the matchmake failed
     */
    enum class MatchmakeState {
        IDLE,
        MATCHMAKING,
        MATCHMADE,
        ERROR
    }

    companion object {
        private const val DEFAULT_GAME_CONFIG_FILE_PATH = "defaultGameConfig.json"
        private const val POLLING_DELAY = 500L
        private const val ANIMATION_DELAY = 1500L
    }
}
