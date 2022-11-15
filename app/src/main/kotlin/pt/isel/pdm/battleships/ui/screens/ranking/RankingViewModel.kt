package pt.isel.pdm.battleships.ui.screens.ranking

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import pt.isel.pdm.battleships.SessionManager
import pt.isel.pdm.battleships.domain.users.User
import pt.isel.pdm.battleships.services.BattleshipsService
import pt.isel.pdm.battleships.services.users.models.getUsers.GetUsersUserModel
import pt.isel.pdm.battleships.services.utils.siren.EmbeddedSubEntity
import pt.isel.pdm.battleships.ui.screens.ranking.RankingViewModel.RankingState.FINISHED
import pt.isel.pdm.battleships.ui.screens.ranking.RankingViewModel.RankingState.GETTING_USERS
import pt.isel.pdm.battleships.ui.screens.ranking.RankingViewModel.RankingState.IDLE
import pt.isel.pdm.battleships.ui.screens.ranking.RankingViewModel.RankingState.LINKS_LOADED
import pt.isel.pdm.battleships.ui.screens.shared.BattleshipsViewModel
import pt.isel.pdm.battleships.ui.utils.launchAndExecuteRequestRetrying

/**
 * View model for the [RankingActivity].
 *
 * @property battleshipsService the service of the battleships application
 * @property sessionManager the manager used to handle the user session
 *
 * @property events the events that occurred in the view model
 */
class RankingViewModel(
    battleshipsService: BattleshipsService,
    sessionManager: SessionManager
) : BattleshipsViewModel(battleshipsService, sessionManager) {

    data class RankingScreenState(
        val state: RankingState = IDLE,
        val users: List<User> = emptyList()
    )

    private var _screenState by mutableStateOf(RankingScreenState())
    val screenState: RankingScreenState
        get() = _screenState

    /**
     * Gets all the users.
     */
    fun getUsers() {
        check(screenState.state == LINKS_LOADED) {
            "The view model is not in the links loaded state"
        }

        _screenState = _screenState.copy(state = GETTING_USERS)

        launchAndExecuteRequestRetrying(
            request = {
                battleshipsService.usersService.getUsers() // TODO query params "$listUsersLink?$SORT_DIRECTION_PARAM=$SORT_DIRECTION_VALUE"
            },
            events = _events,
            onSuccess = { usersData ->
                _screenState = _screenState.copy(
                    users = usersData.entities
                        ?.filterIsInstance<EmbeddedSubEntity<GetUsersUserModel>>()
                        ?.map { entity ->
                            val userProperties = entity.properties
                                ?: throw IllegalStateException(
                                    "The user entity does not have properties"
                                )

                            User(
                                username = userProperties.username,
                                email = userProperties.email,
                                points = userProperties.points
                            )
                        } ?: emptyList(),
                    state = FINISHED
                )
            }
        )
    }

    /**
     * The ranking state.
     *
     * @property IDLE the get games operation is idle
     * @property LINKS_LOADED
     * @property GETTING_USERS the get users operation is in progress
     * @property FINISHED the get users operation has finished
     */
    enum class RankingState {
        IDLE,
        LINKS_LOADED,
        GETTING_USERS,
        FINISHED
    }

    companion object {
        const val SORT_DIRECTION_PARAM = "sortDirection"
        const val SORT_DIRECTION_VALUE = "DESC"
    }
}
