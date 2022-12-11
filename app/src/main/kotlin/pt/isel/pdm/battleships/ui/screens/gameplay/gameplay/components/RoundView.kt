package pt.isel.pdm.battleships.ui.screens.gameplay.gameplay.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.isel.pdm.battleships.R

private const val BORDER_WIDTH = 2
private const val CORNER_CLIP_SIZE = 4
private const val PADDING_SIZE = 4

/**
 * The round view for the gameplay.
 *
 * @param round the round to display
 */
@Composable
fun RoundView(round: Int) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .border(BORDER_WIDTH.dp, Color.Black, RoundedCornerShape(CORNER_CLIP_SIZE.dp))
            .padding(PADDING_SIZE.dp)
    ) {
        Text(text = stringResource(R.string.gameplay_round_text), fontSize = 12.sp)
        Text(text = round.toString(), fontSize = 12.sp)
    }
}

@Preview
@Composable
private fun RoundViewPreview() {
    RoundView(round = 420)
}
