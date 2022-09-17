package pt.isel.pdm.battleships

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

private val SEARCH_PLAYER_TEXT_FIELD_HEIGHT = 60.dp

/**
 * Raking screen.
 * Shows the rankings/leaderboard of the players.
 *
 * @param backToMenuCallback callback to be invoked when the user wants to go back to the menu
 */
@Composable
fun Ranking(backToMenuCallback: () -> Unit) {
    // Fetch the players from the server api

    val playerSearched = remember { mutableStateOf("") }

    // Search specific player text field
    Row {
        TextField(
            value = playerSearched.value,
            onValueChange = { playerSearched.value = it },
            placeholder = { Text(text = "Search player") },
            modifier = Modifier.height(SEARCH_PLAYER_TEXT_FIELD_HEIGHT)
        )

        Button(
            onClick = {
                // Go to `playerSearched`'s position
            },
            modifier = Modifier.height(SEARCH_PLAYER_TEXT_FIELD_HEIGHT)
        ) {
            Text(text = "Search")
        }
    }

    Button(onClick = backToMenuCallback) {
        Text(text = "Back to menu")
    }
}
