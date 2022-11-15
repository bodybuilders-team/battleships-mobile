package pt.isel.pdm.battleships.ui.screens.ranking

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import pt.isel.pdm.battleships.SessionManager
import pt.isel.pdm.battleships.domain.users.User
import pt.isel.pdm.battleships.services.users.UsersService
import pt.isel.pdm.battleships.services.users.models.getUser.GetUserOutputModel
import pt.isel.pdm.battleships.services.utils.siren.EmbeddedSubEntity
import pt.isel.pdm.battleships.ui.screens.ranking.RankingViewModel.RankingState.FINISHED
import pt.isel.pdm.battleships.ui.screens.ranking.RankingViewModel.RankingState.GETTING_USERS
import pt.isel.pdm.battleships.ui.screens.ranking.RankingViewModel.RankingState.IDLE
import pt.isel.pdm.battleships.ui.utils.Event
import pt.isel.pdm.battleships.ui.utils.launchAndExecuteRequestRetrying

/**
 * View model for the [RankingActivity].
 *
 * @property usersService the service used to handle the users
 * @property sessionManager the manager used to handle the user session
 * @property jsonEncoder the JSON formatter
 *
 * @property state the current state of the view model
 * @property users the list of users
 * @property events the events that occurred in the view model
 */
class RankingViewModel(
    private val usersService: UsersService,
    private val sessionManager: SessionManager,
    private val jsonEncoder: Gson
) : ViewModel() {

    var state by mutableStateOf(IDLE)
    var users by mutableStateOf<List<User>>(emptyList())

    private val _events = MutableSharedFlow<Event>()
    val events: SharedFlow<Event> = _events

    /**
     * Gets all the users.
     *
     * @param listUsersLink the link to the list users endpoint
     */
    fun getUsers(listUsersLink: String) {
        check(state == IDLE) { "The view model is not in the idle state" }

        state = GETTING_USERS

        launchAndExecuteRequestRetrying(
            request = {
                usersService.getUsers("$listUsersLink?$SORT_DIRECTION_PARAM=$SORT_DIRECTION_VALUE")
            },
            events = _events,
            onSuccess = { usersData ->
                users = usersData.entities?.map { user ->
                    @Suppress("UNCHECKED_CAST")
                    user as EmbeddedSubEntity<GetUserOutputModel>

                    val userProperties = jsonEncoder.fromJson(
                        jsonEncoder.toJson(user.properties),
                        GetUserOutputModel::class.java
                    )

                    User(
                        username = userProperties.username,
                        email = userProperties.email,
                        points = userProperties.points
                    )
                } ?: emptyList()

                state = FINISHED
            }
        )
    }

    /**
     * The ranking state.
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

    companion object {
        const val SORT_DIRECTION_PARAM = "sortDirection"
        const val SORT_DIRECTION_VALUE = "DESC"
    }
}
