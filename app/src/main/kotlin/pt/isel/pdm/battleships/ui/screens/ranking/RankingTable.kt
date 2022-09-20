package pt.isel.pdm.battleships.ui.screens.ranking

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
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

// TODO: Add constants and make responsive

private val rankingTableCellModifier =
    Modifier
        .width(RANKING_TABLE_CELL_WIDTH.dp)
        .height(RANKING_TABLE_CELL_HEIGHT.dp)
        .border(
            width = RANKING_TABLE_BORDER_WIDTH.dp,
            color = Color.Black
        )

/**
 * Table that shows the ranking of players, displaying their ranking position, username and points.
 *
 * @param players list of players, already sorted by their ranking position
 */
@Composable
fun RankingTable(players: List<Player>) {
    // TODO Allow for scrolling (Lazy column? Yes, that's the one)
    // TODO Allow for pagination AND/OR infinite scrolling (Lazy column? Yes, that's the one)

    val positionLabel = stringResource(id = R.string.ranking_table_label_position)
    val usernameLabel = stringResource(id = R.string.ranking_table_label_username)
    val pointsLabel = stringResource(id = R.string.ranking_table_label_points)

    Column {
        // Label row
        Row {
            Box(modifier = rankingTableCellModifier) {
                Text(text = positionLabel, modifier = Modifier.align(Alignment.Center))
            }
            Box(modifier = rankingTableCellModifier) {
                Text(text = usernameLabel, modifier = Modifier.align(Alignment.Center))
            }
            Box(modifier = rankingTableCellModifier) {
                Text(text = pointsLabel, modifier = Modifier.align(Alignment.Center))
            }
        }

        Column {
            players.forEachIndexed { index, player ->
                Row {
                    // Position cell
                    Box(
                        modifier = rankingTableCellModifier
                            .background(
                                when (index) {
                                    0 -> Gold
                                    1 -> Silver
                                    2 -> Bronze
                                    else -> Color.Unspecified
                                }
                            )
                    ) {
                        Text(
                            text = (index + 1).toString(),
                            modifier = Modifier
                                .offset(RANKING_CELL_OFFSET.dp)
                                .align(Alignment.Center),
                            color = when (index) {
                                in 0..2 -> Color.Black
                                else -> Color.Unspecified
                            }
                        )
                    }

                    // Username cell
                    Box(modifier = rankingTableCellModifier) {
                        Text(
                            text = player.username,
                            modifier = Modifier
                                .offset(RANKING_CELL_OFFSET.dp)
                                .align(Alignment.Center)
                        )
                    }

                    // Points cell
                    Box(modifier = rankingTableCellModifier) {
                        Text(
                            text = player.points.toString(),
                            modifier = Modifier
                                .offset(RANKING_CELL_OFFSET.dp)
                                .align(Alignment.Center)
                        )
                    }
                }
            }
        }
    }
}
