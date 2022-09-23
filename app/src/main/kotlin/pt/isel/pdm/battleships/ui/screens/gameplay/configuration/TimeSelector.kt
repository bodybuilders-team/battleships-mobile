package pt.isel.pdm.battleships.ui.screens.gameplay.configuration

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

private const val TIME_SELECTOR_WIDTH_FACTOR = 0.5f
private const val TIME_SELECTOR_PADDING = 10
private const val TIME_SELECTOR_STEPS = 5

/**
 * A slider that allows the user to select a time in a specified range.
 *
 * @param defaultTime the default time to be selected
 * @param timeRange the range of times that can be selected
 * @param label the label to be displayed above the slider
 * @param onTimeChange the callback to be invoked when the user selects a time
 */
@Composable
fun TimeSelector(
    defaultTime: Int,
    timeRange: IntRange,
    label: String,
    onTimeChange: (Int) -> Unit
) {
    var currentTime by remember { mutableStateOf(defaultTime) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = TIME_SELECTOR_PADDING.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(TIME_SELECTOR_WIDTH_FACTOR),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.h6,
                textAlign = TextAlign.Center
            )
            Text(
                text = "$currentTime s",
                style = MaterialTheme.typography.h6
            )
        }

        Slider(
            value = currentTime.toFloat(),
            onValueChange = {
                currentTime = it.toInt()
                onTimeChange(currentTime)
            },
            valueRange = timeRange.first.toFloat()..timeRange.last.toFloat(),
            steps = TIME_SELECTOR_STEPS
        )
    }
}
