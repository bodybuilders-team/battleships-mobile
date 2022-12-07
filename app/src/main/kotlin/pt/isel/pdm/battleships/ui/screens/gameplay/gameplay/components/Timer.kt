package pt.isel.pdm.battleships.ui.screens.gameplay.gameplay.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.isel.pdm.battleships.R

private const val BORDER_WIDTH = 2
private const val CORNER_CLIP_SIZE = 4
private const val PADDING_SIZE = 4
private const val MINUTES_CHARACTER_PADDING_LENGTH = 2
private const val SECONDS_CHARACTER_PADDING_LENGTH = 2

/**
 * The timer for the gameplay.
 *
 * @param minutes the minutes to display
 * @param seconds the seconds to display
 */
@Composable
fun Timer(minutes: Int, seconds: Int) {
    val minutesString = minutes.toString().padStart(MINUTES_CHARACTER_PADDING_LENGTH, '0')
    val secondsString = seconds.toString().padStart(SECONDS_CHARACTER_PADDING_LENGTH, '0')

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .border(BORDER_WIDTH.dp, Color.Black, RoundedCornerShape(CORNER_CLIP_SIZE.dp))
            .padding(PADDING_SIZE.dp)
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_round_timer_24),
            contentDescription = stringResource(R.string.gameplay_timer_iconDescription)
        )

        Text(
            text = "$minutesString:$secondsString",
            color = if (minutes == 0 && seconds <= 10) Color.Red else Color.Unspecified
        )
    }
}

@Preview
@Composable
private fun TimerPreview() {
    Timer(minutes = 10, seconds = 50)
}
