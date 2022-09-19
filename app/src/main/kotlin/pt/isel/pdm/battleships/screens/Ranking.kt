package pt.isel.pdm.battleships.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import pt.isel.pdm.battleships.MockApi
import pt.isel.pdm.battleships.Player
import pt.isel.pdm.battleships.R

private const val RANKING_TITLE_PADDING = 8
private const val RANKING_TABLE_BORDER_WIDTH = 1
private const val RANKING_TABLE_CELL_WIDTH = 100 // TODO: Make responsive
private const val RANKING_TABLE_CELL_HEIGHT = 30
private val SEARCH_PLAYER_TEXT_FIELD_HEIGHT = 56.dp
private val RANKING_CELL_OFFSET = 5.dp
private const val SEARCH_PLAYER_FIELD_PADDING = 12
private const val SEARCH_PLAYER_FIELD_WIDTH_FACTOR = 0.6f

// TODO: Add constants and make responsive

private val rankingTableCellModifier =
    Modifier
        .width(RANKING_TABLE_CELL_WIDTH.dp)
        .height(RANKING_TABLE_CELL_HEIGHT.dp)
        .border(
            width = RANKING_TABLE_BORDER_WIDTH.dp,
            color = Color.Black
        )

private val Color.Companion.Gold
    get() = Color(0xFFFFCC33)
private val Color.Companion.Silver
    get() = Color(0xFFB5B7BB)
private val Color.Companion.Bronze
    get() = Color(0xFFA05822)

/**
 * Ranking screen.
 * Shows the rankings/leaderboard of the players.
 *
 * The position is based of the player's points.
 *
 * @param backToMenuCallback callback to be invoked when the user wants to go back to the menu
 */
@Composable
fun Ranking(backToMenuCallback: () -> Unit) {
    val players = MockApi.getPlayers().sortedByDescending { it.points }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.ranking_title),
            style = MaterialTheme.typography.h4,
            modifier = Modifier.padding(RANKING_TITLE_PADDING.dp)
        )

        SearchPlayerField {
            // TODO: Go to searched player's position
        }

        RankingTable(players)

        Button(onClick = backToMenuCallback) {
            Text(text = stringResource(id = R.string.back_to_menu_button_text))
        }
    }
}

/**
 * Text field responsible for searching for a specific player in the rankings.
 *
 * @param searchButtonCallback callback to be invoked when the search button is pressed
 */
@Composable
private fun SearchPlayerField(searchButtonCallback: () -> Unit) {
    var playerSearched by remember { mutableStateOf("") }

    val searchPlayerPlaceholderText =
        stringResource(id = R.string.ranking_search_player_placeholder_text)
    val searchPlayerButtonText = stringResource(id = R.string.ranking_search_player_button_text)

    Row(modifier = Modifier.padding(bottom = SEARCH_PLAYER_FIELD_PADDING.dp)) {
        TextField(
            value = playerSearched,
            onValueChange = { playerSearched = it },
            placeholder = { Text(text = searchPlayerPlaceholderText) },
            modifier = Modifier.fillMaxWidth(SEARCH_PLAYER_FIELD_WIDTH_FACTOR)
        )

        Button(
            onClick = searchButtonCallback,
            modifier = Modifier.height(SEARCH_PLAYER_TEXT_FIELD_HEIGHT)
        ) {
            Text(text = searchPlayerButtonText)
        }
    }
}

/**
 * Table that shows the ranking of players, displaying their ranking position, username and points.
 *
 * @param players list of players, already sorted by their ranking position
 */
@Composable
private fun RankingTable(players: List<Player>) {
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
                                    0 -> Color.Gold
                                    1 -> Color.Silver
                                    2 -> Color.Bronze
                                    else -> Color.Unspecified
                                }
                            )
                    ) {
                        Text(
                            text = (index + 1).toString(),
                            modifier = Modifier
                                .offset(RANKING_CELL_OFFSET)
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
                                .offset(RANKING_CELL_OFFSET)
                                .align(Alignment.Center)
                        )
                    }

                    // Points cell
                    Box(modifier = rankingTableCellModifier) {
                        Text(
                            text = player.points.toString(),
                            modifier = Modifier
                                .offset(RANKING_CELL_OFFSET)
                                .align(Alignment.Center)
                        )
                    }
                }
            }
        }
    }
}
