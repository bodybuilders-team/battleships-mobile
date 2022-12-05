package pt.isel.pdm.battleships.ui.screens.ranking.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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

private const val SEARCH_PLAYER_FIELD_PADDING = 12
private const val SEARCH_PLAYER_FIELD_WIDTH_FACTOR = 0.6f

/**
 * Text field responsible for searching for a specific player in the rankings.
 *
 * @param onPlayerNameChanged callback to be invoked when the username in the search field changes
 */
@Composable
fun SearchPlayerField(onPlayerNameChanged: (String) -> Unit) {
    var playerSearched by remember { mutableStateOf("") }

    Row(
        modifier = Modifier.padding(bottom = SEARCH_PLAYER_FIELD_PADDING.dp)
    ) {
        TextField(
            value = playerSearched,
            onValueChange = {
                playerSearched = it
                onPlayerNameChanged(it)
            },
            placeholder = {
                Text(stringResource(R.string.ranking_searchPlayerTextField_placeholderText))
            },
            modifier = Modifier.fillMaxWidth(SEARCH_PLAYER_FIELD_WIDTH_FACTOR)
        )
    }
}
