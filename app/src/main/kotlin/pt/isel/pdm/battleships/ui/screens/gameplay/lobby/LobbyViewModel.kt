package pt.isel.pdm.battleships.ui.screens.gameplay.lobby

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import pt.isel.pdm.battleships.SessionManager
import pt.isel.pdm.battleships.services.BattleshipsService
import pt.isel.pdm.battleships.services.games.models.games.getGames.GetGamesOutput
import pt.isel.pdm.battleships.ui.screens.gameplay.lobby.LobbyViewModel.LobbyState.FINISHED
import pt.isel.pdm.battleships.ui.screens.gameplay.lobby.LobbyViewModel.LobbyState.GETTING_GAMES
import pt.isel.pdm.battleships.ui.screens.gameplay.lobby.LobbyViewModel.LobbyState.IDLE
import pt.isel.pdm.battleships.ui.screens.gameplay.lobby.LobbyViewModel.LobbyState.LINKS_LOADED
import pt.isel.pdm.battleships.ui.screens.shared.BattleshipsViewModel
import pt.isel.pdm.battleships.ui.utils.launchAndExecuteRequestRetrying
import pt.isel.pdm.battleships.ui.utils.navigation.Links

/**
 * View model for the [LobbyActivity].
 *
 * @property state the current state of the view model
 * @property games the list of games
 * @property events the events that can be emitted by the view model
 */
class LobbyViewModel(
    battleshipsService: BattleshipsService,
    sessionManager: SessionManager
) : BattleshipsViewModel(battleshipsService, sessionManager) {

    private var _state by mutableStateOf(IDLE)
    val state
        get() = _state

    private var _games by mutableStateOf<GetGamesOutput?>(null)
    val games
        get() = _games

    /**
     * Gets the list of games.
     */
    fun getGames() {
        check(state == LINKS_LOADED) {
            "The view model is not in the links loaded state"
        }

        _state = GETTING_GAMES

        launchAndExecuteRequestRetrying(
            request = { battleshipsService.gamesService.getGames() },
            events = _events,
            onSuccess = { gamesData ->
                _state = FINISHED
                _games = gamesData
            }
        )
    }

    override fun updateLinks(links: Links) {
        super.updateLinks(links)
        _state = LINKS_LOADED
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
        LINKS_LOADED,
        GETTING_GAMES,
        FINISHED
    }
}
