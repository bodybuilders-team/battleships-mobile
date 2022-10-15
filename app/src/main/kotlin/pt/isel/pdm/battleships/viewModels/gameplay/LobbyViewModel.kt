package pt.isel.pdm.battleships.viewModels.gameplay

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pt.isel.pdm.battleships.SessionManager
import pt.isel.pdm.battleships.services.Result
import pt.isel.pdm.battleships.services.games.GamesService
import pt.isel.pdm.battleships.services.games.dtos.GamesDTO

/**
 * View model for the lobby screen.
 *
 * @property sessionManager the session manager used to handle the user session
 * @property gamesService the game service
 */
class LobbyViewModel(
    private val sessionManager: SessionManager,
    private val gamesService: GamesService
) : ViewModel() {

    var state by mutableStateOf(SearchGameState.SEARCHING)
    var games by mutableStateOf<GamesDTO?>(null)
    var errorMessage: String? by mutableStateOf(null)

    fun getAllGames() {
        viewModelScope.launch {
            games = when (val res = gamesService.getAllGames()) {
                is Result.Success -> {
                    state = SearchGameState.SEARCH_FINISHED
                    res.dto
                }
                is Result.Failure -> {
                    errorMessage = res.error.message
                    state = SearchGameState.ERROR
                    return@launch
                }
            }
        }
    }

    enum class SearchGameState {
        SEARCHING,
        SEARCH_FINISHED,
        ERROR
    }
}
