package pt.isel.pdm.battleships.ui.screens.ranking

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pt.isel.pdm.battleships.SessionManager
import pt.isel.pdm.battleships.services.users.UsersService
import pt.isel.pdm.battleships.services.users.dtos.UsersDTO
import pt.isel.pdm.battleships.services.utils.HTTPResult
import pt.isel.pdm.battleships.ui.screens.ranking.RankingViewModel.RankingState.ERROR
import pt.isel.pdm.battleships.ui.screens.ranking.RankingViewModel.RankingState.FINISHED
import pt.isel.pdm.battleships.ui.screens.ranking.RankingViewModel.RankingState.GETTING_USERS
import pt.isel.pdm.battleships.ui.screens.ranking.RankingViewModel.RankingState.IDLE

/**
 * View model for the RankingActivity.
 *
 * @property usersService the service used to handle the users
 * @property sessionManager the manager used to handle the user session
 *
 * @property state the current state of the view model
 * @property users the list of users
 * @property errorMessage the error message to be displayed
 */
class RankingViewModel(
    private val usersService: UsersService,
    private val sessionManager: SessionManager
) : ViewModel() {

    var state by mutableStateOf(IDLE)
    var users by mutableStateOf<UsersDTO?>(null)
    var errorMessage: String? by mutableStateOf(null)

    /**
     * Gets all the users.
     *
     * @param listUsersLink the link to the list users endpoint
     */
    fun getUsers(listUsersLink: String) {
        if (state != IDLE) return

        viewModelScope.launch {
            users = when ( // TODO: fix this
                val res = usersService.getUsers("$listUsersLink?offset=0&limit=100")
            ) {
                is HTTPResult.Success -> {
                    state = FINISHED
                    res.data
                }
                is HTTPResult.Failure -> {
                    errorMessage = res.error.title
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
     * @property GETTING_USERS the get users operation is in progress
     * @property FINISHED the get users operation has finished
     * @property ERROR the get users operation has failed
     */
    enum class RankingState {
        IDLE,
        GETTING_USERS,
        FINISHED,
        ERROR
    }
}
