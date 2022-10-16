package pt.isel.pdm.battleships.viewModels.gameplay

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pt.isel.pdm.battleships.SessionManager
import pt.isel.pdm.battleships.services.games.GamesService
import pt.isel.pdm.battleships.services.games.dtos.GamesDTO
import pt.isel.pdm.battleships.services.utils.Result
import pt.isel.pdm.battleships.viewModels.gameplay.LobbyViewModel.LobbyState.ERROR
import pt.isel.pdm.battleships.viewModels.gameplay.LobbyViewModel.LobbyState.FINISHED
import pt.isel.pdm.battleships.viewModels.gameplay.LobbyViewModel.LobbyState.GETTING_GAMES

/**
 * View model for the lobby screen.
 *
 * @property sessionManager the session manager used to handle the user session
 * @property gamesService the game service
 * @property state the current state of the view model
 * @property games the list of games
 * @property errorMessage the error message
 */
class LobbyViewModel(
    private val sessionManager: SessionManager,
    private val gamesService: GamesService
) : ViewModel() {

    var state by mutableStateOf(GETTING_GAMES)
    var games by mutableStateOf<GamesDTO?>(null)
    var errorMessage: String? by mutableStateOf(null)

    /**
     * Gets the list of games.
     */
    fun getAllGames() {
        viewModelScope.launch {
            games = when (val res = gamesService.getAllGames()) {
                is Result.Success -> {
                    state = FINISHED
                    res.dto
                }
                is Result.Failure -> {
                    errorMessage = res.error.message
                    state = ERROR
                    return@launch
                }
            }
        }
    }

    /**
     * Represents the lobby state.
     *
     * @property GETTING_GAMES the get games operation is in progress
     * @property FINISHED the get games operation has finished
     * @property ERROR the get games operation has failed
     */
    enum class LobbyState {
        GETTING_GAMES,
        FINISHED,
        ERROR
    }
}
