package pt.isel.pdm.battleships.ui.screens.gameplay.lobby

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import pt.isel.pdm.battleships.SessionManager
import pt.isel.pdm.battleships.services.games.GamesService
import pt.isel.pdm.battleships.services.games.models.games.getGames.GetGamesOutput
import pt.isel.pdm.battleships.ui.screens.gameplay.lobby.LobbyViewModel.LobbyState.FINISHED
import pt.isel.pdm.battleships.ui.screens.gameplay.lobby.LobbyViewModel.LobbyState.GETTING_GAMES
import pt.isel.pdm.battleships.ui.screens.gameplay.lobby.LobbyViewModel.LobbyState.IDLE
import pt.isel.pdm.battleships.ui.utils.Event
import pt.isel.pdm.battleships.ui.utils.launchAndExecuteRequestRetrying

/**
 * View model for the [LobbyActivity].
 *
 * @property gamesService the service that handles the games
 * @property sessionManager the manager used to handle the user session
 *
 * @property state the current state of the view model
 * @property games the list of games
 * @property events the events that can be emitted by the view model
 */
class LobbyViewModel(
    private val gamesService: GamesService,
    private val sessionManager: SessionManager
) : ViewModel() {

    var state by mutableStateOf(IDLE)
    var games by mutableStateOf<GetGamesOutput?>(null)

    private val _events = MutableSharedFlow<Event>()
    val events: SharedFlow<Event> = _events

    /**
     * Gets the list of games.
     *
     * @param listGamesLink the link to the list of games endpoint
     */
    fun getGames(listGamesLink: String) {
        check(state == IDLE) { "The view model is not in the idle state" }

        state = GETTING_GAMES

        launchAndExecuteRequestRetrying(
            request = { gamesService.getGames(listGamesLink = listGamesLink) },
            events = _events,
            onSuccess = { gamesData ->
                state = FINISHED
                games = gamesData
            }
        )
    }

    /**
     * The lobby state.
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
}
