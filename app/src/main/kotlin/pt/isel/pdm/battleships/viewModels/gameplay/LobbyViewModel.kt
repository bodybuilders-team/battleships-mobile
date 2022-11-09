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
import pt.isel.pdm.battleships.services.utils.HTTPResult
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

    var state by mutableStateOf(LobbyState.IDLE)
    var games by mutableStateOf<GamesDTO?>(null)
    var errorMessage: String? by mutableStateOf(null)

    /**
     * Gets the list of games.
     */
    fun getAllGames(listGamesLink: String) {
        if (state != LobbyState.IDLE) return

        viewModelScope.launch {
            games = when (val res = gamesService.getAllGames(listGamesLink)) {
                is HTTPResult.Success -> {
                    state = FINISHED
                    res.data
                }
                is HTTPResult.Failure -> {
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
        IDLE,
        GETTING_GAMES,
        FINISHED,
        ERROR
    }
}
