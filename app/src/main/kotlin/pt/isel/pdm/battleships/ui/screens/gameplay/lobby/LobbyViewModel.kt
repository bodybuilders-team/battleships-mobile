package pt.isel.pdm.battleships.ui.screens.gameplay.lobby

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import pt.isel.pdm.battleships.SessionManager
import pt.isel.pdm.battleships.services.games.GamesService
import pt.isel.pdm.battleships.services.games.dtos.GamesDTO
import pt.isel.pdm.battleships.services.utils.APIResult
import pt.isel.pdm.battleships.ui.screens.gameplay.lobby.LobbyViewModel.LobbyState.FINISHED
import pt.isel.pdm.battleships.ui.screens.gameplay.lobby.LobbyViewModel.LobbyState.GETTING_GAMES
import pt.isel.pdm.battleships.ui.screens.gameplay.lobby.LobbyViewModel.LobbyState.IDLE
import pt.isel.pdm.battleships.ui.utils.HTTPResult
import pt.isel.pdm.battleships.ui.utils.tryExecuteHttpRequest

/**
 * View model for the LobbyActivity.
 *
 * @property gamesService the service that handles the games
 * @property sessionManager the manager used to handle the user session
 *
 * @property state the current state of the view model
 * @property games the list of games
 */
class LobbyViewModel(
    private val gamesService: GamesService,
    private val sessionManager: SessionManager
) : ViewModel() {

    var state by mutableStateOf(IDLE)
    var games by mutableStateOf<GamesDTO?>(null)

    private val _events = MutableSharedFlow<LobbyEvent>()
    val events: SharedFlow<LobbyEvent> = _events

    /**
     * Gets the list of games.
     *
     * @param listGamesLink the link to the list of games endpoint
     */
    fun getAllGames(listGamesLink: String) {
        if (state != IDLE) return

        viewModelScope.launch {
            val httpRes = tryExecuteHttpRequest {
                gamesService.getAllGames(listGamesLink)
            }

            val res = when (httpRes) {
                is HTTPResult.Success -> httpRes.data
                is HTTPResult.Failure -> {
                    _events.emit(LobbyEvent.Error(httpRes.error))
                    state = IDLE
                    return@launch
                }
            }

            when (res) {
                is APIResult.Success -> {
                    state = FINISHED
                    games = res.data
                }
                is APIResult.Failure -> {
                    _events.emit(LobbyEvent.Error(res.error.title))
                    state = IDLE
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
     */
    enum class LobbyState {
        IDLE,
        GETTING_GAMES,
        FINISHED
    }

    /**
     * Represents the events that can be emitted.
     */
    sealed class LobbyEvent {
        class Error(val message: String) : LobbyEvent()
    }
}
