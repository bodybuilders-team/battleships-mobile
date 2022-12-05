package pt.isel.pdm.battleships.ui.screens.ranking.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
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
import pt.isel.pdm.battleships.domain.users.RankedUser
import pt.isel.pdm.battleships.ui.screens.shared.components.LabelCell
import pt.isel.pdm.battleships.ui.screens.shared.components.NormalTableCell
import pt.isel.pdm.battleships.ui.screens.shared.components.TABLE_CELL_HEIGHT
import pt.isel.pdm.battleships.ui.screens.shared.components.TABLE_CELL_WIDTH
import pt.isel.pdm.battleships.ui.theme.Bronze
import pt.isel.pdm.battleships.ui.theme.Gold
import pt.isel.pdm.battleships.ui.theme.Silver

private const val TABLE_BORDER_WIDTH = 2
private const val TABLE_COLUMNS_COUNT = 3
private const val TABLE_ROWS_COUNT = 11

private const val GOLD_RANK = 1
private const val SILVER_RANK = 2
private const val BRONZE_RANK = 3

/**
 * Table that shows the ranking of users, displaying their ranking position, username and points.
 *
 * @param users list of users to show, already sorted by their ranking position
 */
@Composable
fun RankingTableView(users: List<RankedUser>) {
    Column(
        modifier = Modifier
            .width((TABLE_CELL_WIDTH * TABLE_COLUMNS_COUNT).dp)
            .height((TABLE_CELL_HEIGHT * TABLE_ROWS_COUNT).dp)
            .border(TABLE_BORDER_WIDTH.dp, Color.Black)
    ) {
        // Labels
        Row {
            LabelCell(text = stringResource(R.string.ranking_table_label_rank))
            LabelCell(text = stringResource(R.string.ranking_table_label_username))
            LabelCell(text = stringResource(R.string.ranking_table_label_points))
        }

        // Data
        LazyColumn {
            items(users.size) { index ->
                val user = users[index]

                Row {
                    NormalTableCell(
                        text = user.rankByPoints.toString(),
                        textColor = if (user.rankByPoints <= BRONZE_RANK)
                            Color.Black
                        else
                            Color.Unspecified,
                        modifier = Modifier
                            .background(
                                when (user.rankByPoints) {
                                    GOLD_RANK -> Gold
                                    SILVER_RANK -> Silver
                                    BRONZE_RANK -> Bronze
                                    else -> Color.Unspecified
                                }
                            )
                    )
                    NormalTableCell(text = user.username)
                    NormalTableCell(text = user.points.toString())
                }
            }
        }
    }
}
