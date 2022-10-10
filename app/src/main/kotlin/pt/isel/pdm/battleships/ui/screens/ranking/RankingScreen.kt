package pt.isel.pdm.battleships.ui.screens.ranking

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import pt.isel.pdm.battleships.MockApi
import pt.isel.pdm.battleships.R
import pt.isel.pdm.battleships.ui.utils.GoBackButton

private const val RANKING_TITLE_PADDING = 8

/**
 * Ranking screen.
 * Shows the rankings/leaderboard of the players.
 *
 * The position is based of the player's points.
 *
 * @param navController the navigation controller
 */
@Composable
fun RankingScreen(navController: NavController) {
    val players = MockApi.getPlayers().sortedByDescending { it.points }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = stringResource(R.string.ranking_title),
            style = MaterialTheme.typography.h4,
            modifier = Modifier.padding(RANKING_TITLE_PADDING.dp)
        )

        SearchPlayerField {
            // TODO: Go to searched player's position
        }

        RankingTableView(players)

        GoBackButton(navController)
    }
}