package pt.isel.pdm.battleships.ui.screens.ranking

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import pt.isel.pdm.battleships.MockApi
import pt.isel.pdm.battleships.R
import pt.isel.pdm.battleships.RankedPlayer
import pt.isel.pdm.battleships.ui.utils.GoBackButton

private const val RANKING_TITLE_PADDING = 8

/**
 * Ranking screen.
 * Shows the rankings/leaderboard of the players.
 *
 * The position is based of the player's points.
 *
 * @param onBackButtonClicked callback to be invoked when the back button is clicked
 */
@Composable
fun RankingScreen(onBackButtonClicked: () -> Unit) {
    val fetchedPlayers = MockApi.getPlayers()
        .sortedByDescending { it.points }
        .mapIndexed { index, rankedPlayer ->
            RankedPlayer(
                username = rankedPlayer.username,
                points = rankedPlayer.points,
                position = index + 1
            )
        }

    var players by remember { mutableStateOf(fetchedPlayers) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = stringResource(R.string.ranking_title),
            style = MaterialTheme.typography.h4,
            modifier = Modifier.padding(RANKING_TITLE_PADDING.dp)
        )

        SearchPlayerField { searchedName ->
            players = fetchedPlayers.filter { player ->
                player.username.contains(searchedName)
            }
        }

        RankingTableView(players)

        GoBackButton(onClick = onBackButtonClicked)
    }
}
