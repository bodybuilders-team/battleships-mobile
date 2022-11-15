package pt.isel.pdm.battleships.ui.utils.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp

private const val DEFAULT_TEXT = "Loading..."
private const val STROKE_WIDTH = 4

/**
 * A loading spinner that rotates infinitely.
 * Useful for indicating that a process is running.
 */
@Composable
fun LoadingSpinner(text: String = DEFAULT_TEXT) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(strokeWidth = STROKE_WIDTH.dp)
        Text(text = text)
    }
}
