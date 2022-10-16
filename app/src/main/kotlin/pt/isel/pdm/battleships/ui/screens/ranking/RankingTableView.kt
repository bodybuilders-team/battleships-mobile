package pt.isel.pdm.battleships.ui.screens.ranking

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import pt.isel.pdm.battleships.R
import pt.isel.pdm.battleships.RankedPlayer
import pt.isel.pdm.battleships.ui.theme.Bronze
import pt.isel.pdm.battleships.ui.theme.Gold
import pt.isel.pdm.battleships.ui.theme.Silver
import pt.isel.pdm.battleships.ui.utils.TABLE_CELL_HEIGHT
import pt.isel.pdm.battleships.ui.utils.TABLE_CELL_WIDTH
import pt.isel.pdm.battleships.ui.utils.TableCell

private const val TABLE_BORDER_WIDTH = 5

/**
 * Table that shows the ranking of players, displaying their ranking position, username and points.
 *
 * @param players list of players to show, already sorted by their ranking position
 */
@Composable
fun RankingTableView(players: List<RankedPlayer>) {
    LazyColumn(
        modifier = Modifier
            .width((TABLE_CELL_WIDTH * 3).dp)
            .height((TABLE_CELL_HEIGHT * 11).dp)
            .border(TABLE_BORDER_WIDTH.dp, Color.Black)
    ) {
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
                    text = player.position.toString(),
                    textColor = if (player.position <= 3) Color.Black else Color.Unspecified,
                    modifier = Modifier
                        .background(
                            when (player.position) {
                                1 -> Gold
                                2 -> Silver
                                3 -> Bronze
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
