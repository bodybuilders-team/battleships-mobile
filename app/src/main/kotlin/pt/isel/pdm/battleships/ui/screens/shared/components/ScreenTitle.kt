package pt.isel.pdm.battleships.ui.screens.shared.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

private const val TITLE_PADDING = 8

/**
 * Displays the screen title.
 *
 * @param title the title to be displayed
 */
@Composable
fun ScreenTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.h4,
        modifier = Modifier.padding(TITLE_PADDING.dp)
    )
}
