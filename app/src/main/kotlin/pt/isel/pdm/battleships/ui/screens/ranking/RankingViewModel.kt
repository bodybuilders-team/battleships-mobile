package pt.isel.pdm.battleships.ui.screens.ranking

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import pt.isel.pdm.battleships.domain.users.User
import pt.isel.pdm.battleships.service.BattleshipsService
import pt.isel.pdm.battleships.service.services.users.models.getUsers.GetUsersUserModel
import pt.isel.pdm.battleships.session.SessionManager
import pt.isel.pdm.battleships.ui.screens.BattleshipsViewModel
import pt.isel.pdm.battleships.ui.screens.ranking.RankingViewModel.RankingState.FINISHED
import pt.isel.pdm.battleships.ui.screens.ranking.RankingViewModel.RankingState.GETTING_USERS
import pt.isel.pdm.battleships.ui.screens.ranking.RankingViewModel.RankingState.IDLE
import pt.isel.pdm.battleships.ui.screens.ranking.RankingViewModel.RankingState.LINKS_LOADED
import pt.isel.pdm.battleships.ui.screens.shared.launchAndExecuteRequestThrowing
import pt.isel.pdm.battleships.ui.screens.shared.navigation.Links
import pt.isel.pdm.battleships.ui.screens.shared.navigation.Rels

/**
 * View model for the [RankingActivity].
 *
 * @property battleshipsService the service used to handle the battleships game
 * @property sessionManager the manager used to handle the user session
 *
 * @property users the list of users
 * @property state the current state of the view model
 */
class RankingViewModel(
    battleshipsService: BattleshipsService,
    sessionManager: SessionManager
) : BattleshipsViewModel(battleshipsService, sessionManager) {

    private var _users by mutableStateOf(emptyList<User>())
    private var _state: RankingState by mutableStateOf(IDLE)

    val users: List<User>
        get() = _users

    val state
        get() = _state

    /**
     * Gets all the users.
     */
    fun getUsers() {
        check(state == LINKS_LOADED) { "The view model is not in the links loaded state" }

        _state = GETTING_USERS

        launchAndExecuteRequestThrowing(
            request = {
                battleshipsService.usersService.getUsers(
                    sortParam = SORT_DIRECTION_PARAM,
                    sortValue = SORT_DIRECTION_VALUE
                )
            },
            events = _events,
            onSuccess = { usersData ->
                _users = usersData.embeddedSubEntities<GetUsersUserModel>(Rels.ITEM, Rels.USER)
                    .map { entity ->
                        val userProperties = entity.properties
                            ?: throw IllegalStateException(
                                "The user entity does not have properties"
                            )

                        User(
                            username = userProperties.username,
                            email = userProperties.email,
                            points = userProperties.points,
                            numberOfGamesPlayed = userProperties.numberOfGamesPlayed
                        )
                    }

                _state = FINISHED
            }
        )
    }

    /**
     * Updates the links.
     *
     * @param links the links to update
     */
    override fun updateLinks(links: Links) {
        super.updateLinks(links)
        _state = LINKS_LOADED
    }

    /**
     * The state of the [RankingViewModel].
     *
     * @property IDLE the initial state
     * @property LINKS_LOADED the state when the links are loaded
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
