package pt.isel.pdm.battleships.ui.screens.ranking

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import pt.isel.pdm.battleships.SessionManager
import pt.isel.pdm.battleships.services.users.UsersService
import pt.isel.pdm.battleships.services.users.dtos.UsersDTO
import pt.isel.pdm.battleships.services.utils.APIResult
import pt.isel.pdm.battleships.ui.screens.ranking.RankingViewModel.RankingState.FINISHED
import pt.isel.pdm.battleships.ui.screens.ranking.RankingViewModel.RankingState.GETTING_USERS
import pt.isel.pdm.battleships.ui.screens.ranking.RankingViewModel.RankingState.IDLE
import pt.isel.pdm.battleships.ui.utils.HTTPResult
import pt.isel.pdm.battleships.ui.utils.tryExecuteHttpRequest

/**
 * View model for the RankingActivity.
 *
 * @property usersService the service used to handle the users
 * @property sessionManager the manager used to handle the user session
 *
 * @property state the current state of the view model
 * @property users the list of users
 */
class RankingViewModel(
    private val usersService: UsersService,
    private val sessionManager: SessionManager
) : ViewModel() {

    var state by mutableStateOf(IDLE)
    var users by mutableStateOf<UsersDTO?>(null)

    private val _events = MutableSharedFlow<RankingEvent>()
    val events: SharedFlow<RankingEvent> = _events

    /**
     * Gets all the users.
     *
     * @param listUsersLink the link to the list users endpoint
     */
    fun getUsers(listUsersLink: String) {
        if (state != IDLE) return

        state = GETTING_USERS

        viewModelScope.launch {
            val httpRes = tryExecuteHttpRequest {
                usersService.getUsers("$listUsersLink?offset=0&limit=100")
            }

            val res = when (httpRes) {
                is HTTPResult.Success -> httpRes.data
                is HTTPResult.Failure -> {
                    _events.emit(RankingEvent.Error(httpRes.error))
                    state = IDLE
                    return@launch
                }
            }

            when (res) {
                is APIResult.Success -> {
                    users = res.data
                    state = FINISHED
                }
                is APIResult.Failure -> {
                    _events.emit(RankingEvent.Error(res.error.title))
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
     * @property GETTING_USERS the get users operation is in progress
     * @property FINISHED the get users operation has finished
     */
    enum class RankingState {
        IDLE,
        GETTING_USERS,
        FINISHED
    }

    /**
     * Represents the events that can be emitted.
     */
    sealed class RankingEvent {
        class Error(val message: String) : RankingEvent()
    }
}
