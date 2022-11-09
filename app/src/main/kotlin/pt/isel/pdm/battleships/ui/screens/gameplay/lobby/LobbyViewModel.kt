package pt.isel.pdm.battleships.ui.screens.gameplay.lobby

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
import pt.isel.pdm.battleships.ui.screens.gameplay.lobby.LobbyViewModel.LobbyState.ERROR
import pt.isel.pdm.battleships.ui.screens.gameplay.lobby.LobbyViewModel.LobbyState.FINISHED
import pt.isel.pdm.battleships.ui.screens.gameplay.lobby.LobbyViewModel.LobbyState.GETTING_GAMES
import pt.isel.pdm.battleships.ui.screens.gameplay.lobby.LobbyViewModel.LobbyState.IDLE

/**
 * View model for the LobbyActivity.
 *
 * @property gamesService the service that handles the games
 * @property sessionManager the manager used to handle the user session
 *
 * @property state the current state of the view model
 * @property games the list of games
 * @property errorMessage the error message to be displayed
 */
class LobbyViewModel(
    private val gamesService: GamesService,
    private val sessionManager: SessionManager
) : ViewModel() {

    var state by mutableStateOf(IDLE)
    var games by mutableStateOf<GamesDTO?>(null)
    var errorMessage: String? by mutableStateOf(null)

    /**
     * Gets the list of games.
     *
     * @param listGamesLink the link to the list of games endpoint
     */
    fun getAllGames(listGamesLink: String) {
        if (state != IDLE) return

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
     * @property IDLE the get games operation is idle
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
