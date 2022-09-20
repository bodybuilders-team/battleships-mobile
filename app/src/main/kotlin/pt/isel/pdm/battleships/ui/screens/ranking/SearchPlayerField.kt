package pt.isel.pdm.battleships.ui.screens.ranking

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import pt.isel.pdm.battleships.R

private const val SEARCH_PLAYER_TEXT_FIELD_HEIGHT = 56
private const val SEARCH_PLAYER_FIELD_PADDING = 12
private const val SEARCH_PLAYER_FIELD_WIDTH_FACTOR = 0.6f

/**
 * Text field responsible for searching for a specific player in the rankings.
 *
 * @param searchButtonCallback callback to be invoked when the search button is pressed
 */
@Composable
fun SearchPlayerField(searchButtonCallback: () -> Unit) {
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
            modifier = Modifier.height(SEARCH_PLAYER_TEXT_FIELD_HEIGHT.dp)
        ) {
            Text(text = searchPlayerButtonText)
        }
    }
}
