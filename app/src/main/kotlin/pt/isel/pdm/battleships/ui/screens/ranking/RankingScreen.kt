package pt.isel.pdm.battleships.ui.screens.ranking

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import pt.isel.pdm.battleships.R
import pt.isel.pdm.battleships.domain.users.User
import pt.isel.pdm.battleships.ui.BattleshipsScreen
import pt.isel.pdm.battleships.ui.screens.ranking.RankingViewModel.RankingState
import pt.isel.pdm.battleships.ui.screens.ranking.components.RankingTableView
import pt.isel.pdm.battleships.ui.screens.ranking.components.SearchPlayerField
import pt.isel.pdm.battleships.ui.utils.components.GoBackButton
import pt.isel.pdm.battleships.ui.utils.components.LoadingSpinner
import pt.isel.pdm.battleships.ui.utils.components.ScreenTitle

/**
 * Ranking screen.
 *
 * @param state the current state of the ranking
 * @param users the list of users
 * @param onBackButtonClicked callback to be invoked when the back button is clicked
 */
@Composable
fun RankingScreen(
    state: RankingState,
    users: List<User>,
    onBackButtonClicked: () -> Unit
) {
    BattleshipsScreen {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            ScreenTitle(title = stringResource(id = R.string.ranking_title))

            var filteredUsers by remember {
                mutableStateOf(
                    users.mapIndexed { index, user -> user.toRankedUser(rank = index + 1) }
                )
            }
            LaunchedEffect(users) {
                filteredUsers = users.mapIndexed { index, user ->
                    user.toRankedUser(rank = index + 1)
                }
            }

            SearchPlayerField { searchedName ->
                filteredUsers = users
                    .mapIndexed { index, user -> user.toRankedUser(rank = index + 1) }
                    .filter { user -> user.username.contains(searchedName) }
            }

            when (state) {
                RankingState.GETTING_USERS, RankingState.IDLE -> LoadingSpinner()
                RankingState.FINISHED -> RankingTableView(filteredUsers)
            }

            GoBackButton(onClick = onBackButtonClicked)
        }
    }
}
