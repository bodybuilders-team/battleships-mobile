package pt.isel.pdm.battleships.viewModels.gameplay

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
import pt.isel.pdm.battleships.services.utils.Result
import pt.isel.pdm.battleships.services.utils.siren.EmbeddedLink
import pt.isel.pdm.battleships.viewModels.gameplay.QuickPlayViewModel.QuickPlayState.ERROR
import pt.isel.pdm.battleships.viewModels.gameplay.QuickPlayViewModel.QuickPlayState.MATCHMADE
import pt.isel.pdm.battleships.viewModels.gameplay.QuickPlayViewModel.QuickPlayState.MATCHMAKING

/**
 * View model for the quick play activity.
 *
 * @property sessionManager the session manager used to handle the user session
 * @property gamesService the game service
 * @param jsonFormatter the json formatter
 * @param assetManager the asset manager
 *
 * @property state the current state of the quick play activity
 * @property gameLink the id of the game that was created
 * @property errorMessage the error message to display
 */
class QuickPlayViewModel(
    private val sessionManager: SessionManager,
    private val gamesService: GamesService,
    jsonFormatter: Gson,
    assetManager: AssetManager
) : ViewModel() {

    var state by mutableStateOf(MATCHMAKING)
    var gameLink: String? by mutableStateOf(null)
    var errorMessage: String? by mutableStateOf(null)

    // TODO: change this
    private val gameConfigDTO = jsonFormatter.fromJson<GameConfigDTO>(
        JsonReader(assetManager.open(DEFAULT_GAME_CONFIG_FILE_PATH).reader()),
        GameConfigDTO::class.java
    )

    /**
     * Matchmakes a game with the default configuration.
     */
    fun matchmake() {
        viewModelScope.launch {
            val token = sessionManager.token ?: throw IllegalStateException("No token found")

            val matchmakeGameLink = when (
                val res = gamesService.matchmake(token, gameConfigDTO)
            ) {
                is Result.Success -> {
                    val properties = res.data.properties
                        ?: throw IllegalStateException("Game properties are null")
                    val entities = res.data.entities
                        ?: throw IllegalStateException("Game entities are null")
                    val matchGameLink = res.data.entities
                        .filterIsInstance<EmbeddedLink>()
                        .first { it.rel.contains("game") }.href.path

                    gameLink = matchGameLink
                    if (!properties.wasCreated) {
                        state = MATCHMADE
                        return@launch
                    }
                    matchGameLink
                }
                is Result.Failure -> {
                    errorMessage = res.error.message
                    state = ERROR
                    return@launch
                }
            }

            while (state != MATCHMADE) {
                when (
                    val res = gamesService.getGameState(token, matchmakeGameLink)
                ) {
                    is Result.Success -> {
                        val properties = res.data.properties
                            ?: throw IllegalStateException("Game state properties are null")

                        if (properties.phase == "WAITING_FOR_PLAYERS") {
                            delay(POLLING_DELAY)
                        } else {
                            state = MATCHMADE
                        }
                    }
                    is Result.Failure -> {
                        errorMessage = res.error.message
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
    enum class QuickPlayState {
        MATCHMAKING,
        MATCHMADE,
        ERROR
    }

    companion object {
        private const val DEFAULT_GAME_CONFIG_FILE_PATH = "defaultGameConfig.json"
        private const val POLLING_DELAY = 500L
    }
}
