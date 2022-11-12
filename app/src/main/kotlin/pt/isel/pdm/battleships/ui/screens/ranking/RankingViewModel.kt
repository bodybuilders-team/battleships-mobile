package pt.isel.pdm.battleships.ui.screens.ranking

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import pt.isel.pdm.battleships.SessionManager
import pt.isel.pdm.battleships.domain.users.User
import pt.isel.pdm.battleships.services.users.UsersService
import pt.isel.pdm.battleships.services.users.dtos.UserDTOProperties
import pt.isel.pdm.battleships.services.utils.APIResult
import pt.isel.pdm.battleships.services.utils.siren.EmbeddedSubEntity
import pt.isel.pdm.battleships.ui.screens.ranking.RankingViewModel.RankingState.FINISHED
import pt.isel.pdm.battleships.ui.screens.ranking.RankingViewModel.RankingState.GETTING_USERS
import pt.isel.pdm.battleships.ui.screens.ranking.RankingViewModel.RankingState.IDLE
import pt.isel.pdm.battleships.ui.utils.HTTPResult
import pt.isel.pdm.battleships.ui.utils.tryExecuteHttpRequest

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
                usersService.getUsers("$listUsersLink?$SORT_DIRECTION_PARAM=$SORT_DIRECTION_VALUE")
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
                    val usersData = res.data
                    users = usersData.entities?.map { userDTO ->
                        @Suppress("UNCHECKED_CAST")
                        userDTO as EmbeddedSubEntity<UserDTOProperties>

                        val userProperties = jsonEncoder.fromJson(
                            jsonEncoder.toJson(userDTO.properties),
                            UserDTOProperties::class.java
                        )
                        User(
                            username = userProperties.username,
                            email = userProperties.email,
                            points = userProperties.points
                        )
                    } ?: emptyList()

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

        /**
         * An error event.
         *
         * @property message the error message
         */
        class Error(val message: String) : RankingEvent()
    }

    companion object {
        const val SORT_DIRECTION_PARAM = "sortDirection"
        const val SORT_DIRECTION_VALUE = "DESC"
    }
}
