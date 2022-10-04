package pt.isel.pdm.battleships.ui.screens.ranking

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import pt.isel.pdm.battleships.Player
import pt.isel.pdm.battleships.R
import pt.isel.pdm.battleships.ui.theme.Bronze
import pt.isel.pdm.battleships.ui.theme.Gold
import pt.isel.pdm.battleships.ui.theme.Silver

private const val RANKING_CELL_OFFSET = 5
private const val RANKING_TABLE_BORDER_WIDTH = 1
private const val RANKING_TABLE_CELL_WIDTH = 100
private const val RANKING_TABLE_CELL_HEIGHT = 30

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
                        ),
                    textModifier = Modifier.offset(RANKING_CELL_OFFSET.dp)
                )
                TableCell(
                    text = player.username,
                    textModifier = Modifier.offset(RANKING_CELL_OFFSET.dp)
                )
                TableCell(
                    text = player.points.toString(),
                    textModifier = Modifier.offset(RANKING_CELL_OFFSET.dp)
                )
            }
        }
    }
}

/**
 * Table cell that displays a text.
 *
 * @param modifier modifier to be applied to the cell
 * @param text text to be displayed
 * @param textColor color of the text
 * @param textModifier modifier to be applied to the text
 */
@Composable
private fun TableCell(
    modifier: Modifier = Modifier,
    text: String,
    textModifier: Modifier = Modifier,
    textColor: Color = Color.Unspecified
) {
    Box(
        modifier = Modifier
            .width(RANKING_TABLE_CELL_WIDTH.dp)
            .height(RANKING_TABLE_CELL_HEIGHT.dp)
            .border(RANKING_TABLE_BORDER_WIDTH.dp, Color.Black)
            .then(modifier)
    ) {
        Text(
            text = text,
            color = textColor,
            modifier = textModifier.align(Alignment.Center)
        )
    }
}
