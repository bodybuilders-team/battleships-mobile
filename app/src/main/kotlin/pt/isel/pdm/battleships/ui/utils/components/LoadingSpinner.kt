package pt.isel.pdm.battleships.ui.utils.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import pt.isel.pdm.battleships.R

private const val STROKE_WIDTH = 4

/**
 * A loading spinner that rotates infinitely.
 * Useful for indicating that a process is running.
 *
 * @param text the text to be shown below the spinner
 */
@Composable
fun LoadingSpinner(text: String = stringResource(id = R.string.loading)) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(strokeWidth = STROKE_WIDTH.dp)
        Text(text = text)
    }
}
