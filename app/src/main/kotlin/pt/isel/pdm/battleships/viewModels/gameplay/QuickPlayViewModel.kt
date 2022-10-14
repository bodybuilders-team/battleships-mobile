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
import pt.isel.pdm.battleships.services.Result
import pt.isel.pdm.battleships.services.games.GamesService
import pt.isel.pdm.battleships.services.games.dtos.GameConfigDTO
import pt.isel.pdm.battleships.viewModels.DEFAULT_GAME_CONFIG_FILE_PATH
import pt.isel.pdm.battleships.viewModels.gameplay.QuickPlayViewModel.QuickPlayState.ERROR
import pt.isel.pdm.battleships.viewModels.gameplay.QuickPlayViewModel.QuickPlayState.MATCHMADE
import pt.isel.pdm.battleships.viewModels.gameplay.QuickPlayViewModel.QuickPlayState.MATCHMAKING

/**
 * View model for the quick play activity.
 *
 * @property sessionManager the session manager used to handle the user session
 * @property gamesService the game service
 */
class QuickPlayViewModel(
    private val sessionManager: SessionManager,
    private val gamesService: GamesService,
    jsonFormatter: Gson,
    assetManager: AssetManager
) : ViewModel() {

    var state by mutableStateOf(MATCHMAKING)
    var gameId: Int? by mutableStateOf(null)
    var errorMessage: String? by mutableStateOf(null)

    // TODO: change this
    private val gameConfigDTO = jsonFormatter.fromJson<GameConfigDTO>(
        JsonReader(assetManager.open(DEFAULT_GAME_CONFIG_FILE_PATH).reader()),
        GameConfigDTO::class.java
    )

    // TODO: Comment this
    fun matchmake() {
        viewModelScope.launch {
            val matchmakeGameId = when (
                val res = gamesService.matchmake(sessionManager.token!!, gameConfigDTO)
            ) {
                is Result.Success -> {
                    if (res.dto.wasCreated) {
                        res.dto.game.id
                    } else {
                        state = MATCHMADE
                        return@launch
                    }
                }
                is Result.Failure -> {
                    errorMessage = res.error.message
                    state = ERROR
                    return@launch
                }
            }

            gameId = matchmakeGameId

            while (state != MATCHMADE) {
                when (
                    val res = gamesService.getGameState(sessionManager.token!!, matchmakeGameId)
                ) {
                    is Result.Success -> {
                        if (res.dto.phase == "WAITING_FOR_PLAYERS") {
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
        private const val POLLING_DELAY = 500L
    }
}
