package pt.isel.pdm.battleships.ui.screens.gameplay.gameplay.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.isel.pdm.battleships.R

private const val PADDING_SIZE = 4

/**
 * The timer for the gameplay.
 *
 * @param round the round to display
 */
@Composable
fun Round(round: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(PADDING_SIZE.dp)
    ) {
        Text(text = "${stringResource(R.string.gameplay_round_text)}: ")
        Text(text = round.toString())
    }
}

@Preview
@Composable
private fun RoundPreview() {
    Round(round = 2)
}
