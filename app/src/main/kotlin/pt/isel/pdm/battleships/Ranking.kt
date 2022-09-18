package pt.isel.pdm.battleships

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val SEARCH_PLAYER_TEXT_FIELD_HEIGHT = 60.dp

private val rankingTableCellModifier = Modifier.width(100.dp).height(30.dp).border(
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
            Text(text = "Back to menu")
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
    val playerSearched = remember { mutableStateOf("") }

    Row {
        TextField(
            value = playerSearched.value,
            onValueChange = { playerSearched.value = it },
            placeholder = { Text(text = "Search player") },
            modifier = Modifier.height(SEARCH_PLAYER_TEXT_FIELD_HEIGHT)
        )

        Button(
            onClick = searchButtonCallback,
            modifier = Modifier.height(SEARCH_PLAYER_TEXT_FIELD_HEIGHT)
        ) {
            Text(text = "Search")
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
    // TODO Allow for scrolling (Lazy column?)
    // TODO Allow for pagination AND/OR infinite scrolling (Lazy column?)

    Column {
        // Label row
        Row {
            Box(modifier = rankingTableCellModifier) {
                Text(text = "Position", modifier = Modifier.align(Alignment.Center))
            }
            Box(modifier = rankingTableCellModifier) {
                Text(text = "Username", modifier = Modifier.align(Alignment.Center))
            }
            Box(modifier = rankingTableCellModifier) {
                Text(text = "Points", modifier = Modifier.align(Alignment.Center))
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
                                .offset(5.dp)
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
                            modifier = Modifier.offset(5.dp)
                        )
                    }

                    // Points cell
                    Box(modifier = rankingTableCellModifier) {
                        Text(
                            text = player.points.toString(),
                            modifier = Modifier.offset(5.dp)
                        )
                    }
                }
            }
        }
    }
}
