package pt.isel.pdm.battleships.ui.screens.ranking

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import pt.isel.pdm.battleships.Player
import pt.isel.pdm.battleships.R
import pt.isel.pdm.battleships.ui.theme.Bronze
import pt.isel.pdm.battleships.ui.theme.Gold
import pt.isel.pdm.battleships.ui.theme.Silver
import pt.isel.pdm.battleships.ui.utils.TableCell

/**
 * Table that shows the ranking of players, displaying their ranking position, username and points.
 *
 * @param players list of players, already sorted by their ranking position
 */
@Composable
fun RankingTableView(players: List<Player>) {
    LazyColumn {
        // Headers
        item {
            Row {
                TableCell(text = stringResource(id = R.string.ranking_table_label_position))
                TableCell(text = stringResource(id = R.string.ranking_table_label_username))
                TableCell(text = stringResource(id = R.string.ranking_table_label_points))
            }
        }

        // Data
        items(players.size) { index ->
            val player = players[index]
            Row {
                TableCell(
                    text = (index + 1).toString(),
                    textColor = if (index < 3) Color.Black else Color.Unspecified,
                    modifier = Modifier
                        .background(
                            when (index) {
                                0 -> Gold
                                1 -> Silver
                                2 -> Bronze
                                else -> Color.Unspecified
                            }
                        )
                )
                TableCell(text = player.username)
                TableCell(text = player.points.toString())
            }
        }
    }
}
