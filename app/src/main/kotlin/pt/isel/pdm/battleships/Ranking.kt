package pt.isel.pdm.battleships

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

private val SEARCH_PLAYER_TEXT_FIELD_HEIGHT = 60.dp
private val RANKING_CELL_OFFSET = 5.dp

private val rankingTableCellModifier =
    Modifier
        .width(100.dp)
        .height(30.dp)
        .border(
            1.dp,
            color = Color.Red
        )

private val Color.Companion.Gold
    get() = Color(0xFFFFDF00)
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

    Column(modifier = Modifier.fillMaxWidth()) {
        SearchPlayerField {
            // Go to searched player's position
        }

        Box(modifier = Modifier.align(Alignment.CenterHorizontally)) {
            RankingTable(players)
        }

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

    Row {
        TextField(
            value = playerSearched,
            onValueChange = { playerSearched = it },
            placeholder = { Text(text = searchPlayerPlaceholderText) },
            modifier = Modifier.height(SEARCH_PLAYER_TEXT_FIELD_HEIGHT)
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
                            modifier = Modifier.offset(RANKING_CELL_OFFSET)
                        )
                    }

                    // Points cell
                    Box(modifier = rankingTableCellModifier) {
                        Text(
                            text = player.points.toString(),
                            modifier = Modifier.offset(RANKING_CELL_OFFSET)
                        )
                    }
                }
            }
        }
    }
}
