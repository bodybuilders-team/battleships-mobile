package pt.isel.pdm.battleships.ui.screens.ranking.components

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
import pt.isel.pdm.battleships.services.users.dtos.UsersDTO
import pt.isel.pdm.battleships.services.utils.siren.EmbeddedLink
import pt.isel.pdm.battleships.ui.theme.Bronze
import pt.isel.pdm.battleships.ui.theme.Gold
import pt.isel.pdm.battleships.ui.theme.Silver
import pt.isel.pdm.battleships.ui.utils.TABLE_CELL_HEIGHT
import pt.isel.pdm.battleships.ui.utils.TABLE_CELL_WIDTH
import pt.isel.pdm.battleships.ui.utils.TableCell

private const val TABLE_BORDER_WIDTH = 5
private const val TABLE_COLUMNS_COUNT = 3
private const val TABLE_ROWS_COUNT = 11

/**
 * Table that shows the ranking of users, displaying their ranking position, username and points.
 *
 * @param users list of users to show, already sorted by their ranking position
 */
@Composable
fun RankingTableView(users: UsersDTO) {
    LazyColumn(
        modifier = Modifier
            .width((TABLE_CELL_WIDTH * TABLE_COLUMNS_COUNT).dp)
            .height((TABLE_CELL_HEIGHT * TABLE_ROWS_COUNT).dp)
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
        val totalCount = users.properties?.totalCount ?: 0
        items(totalCount) { index ->
            val user = users.entities?.get(index) as EmbeddedLink
            val position = index + 1
            val username = user.title!!
            Row {
                TableCell(
                    text = position.toString(),
                    textColor = if (position <= 3) Color.Black else Color.Unspecified,
                    modifier = Modifier
                        .background(
                            when (position) {
                                1 -> Gold
                                2 -> Silver
                                3 -> Bronze
                                else -> Color.Unspecified
                            }
                        )
                )
                TableCell(text = username)
                // TODO: TableCell(text = player.points.toString())
            }
        }
    }
}
