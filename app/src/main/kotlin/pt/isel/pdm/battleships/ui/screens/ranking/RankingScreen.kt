package pt.isel.pdm.battleships.ui.screens.ranking

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import pt.isel.pdm.battleships.R
import pt.isel.pdm.battleships.services.users.dtos.UsersDTO
import pt.isel.pdm.battleships.ui.BattleshipsScreen
import pt.isel.pdm.battleships.ui.screens.ranking.RankingViewModel.RankingState
import pt.isel.pdm.battleships.ui.screens.ranking.components.RankingTableView
import pt.isel.pdm.battleships.ui.screens.ranking.components.SearchPlayerField
import pt.isel.pdm.battleships.ui.utils.GoBackButton
import pt.isel.pdm.battleships.ui.utils.ScreenTitle

/**
 * Ranking screen.
 *
 * @param state the current state of the ranking
 * @param users the list of users
 * @param errorMessage the error message to be displayed
 * @param onBackButtonClicked callback to be invoked when the back button is clicked
 */
@Composable
fun RankingScreen(
    state: RankingState,
    users: UsersDTO?,
    errorMessage: String?,
    onBackButtonClicked: () -> Unit
) {
    BattleshipsScreen {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            ScreenTitle(title = stringResource(id = R.string.ranking_title))

            SearchPlayerField { searchedName ->
                /*TODO: players = fetchedPlayers.filter { player ->
                    player.username.contains(searchedName)
                }*/
            }

            when (state) {
                RankingState.GETTING_USERS, RankingState.IDLE -> Text(text = "Searching...")
                RankingState.FINISHED -> RankingTableView(
                    users ?: throw IllegalStateException(
                        "Users cannot be null when state is FINISHED"
                    )
                )
                RankingState.ERROR -> Text(text = errorMessage ?: "Unknown error")
            }

            GoBackButton(onClick = onBackButtonClicked)
        }
    }
}
