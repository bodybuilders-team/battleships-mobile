package pt.isel.pdm.battleships.ui.screens.gameplay.configuration

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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

private const val INT_SELECTOR_WIDTH_FACTOR = 0.5f

/**
 * A slider that allows the user to select an integer value.
 *
 * @param defaultValue the default value of the slider
 * @param valueRange the range of values that the slider can take
 * @param label the label of the slider
 * @param valueLabel the label of the value of the slider
 * @param onValueChange the callback that will be invoked when the value of the slider changes
 */
@Composable
fun IntSelector(
    defaultValue: Int,
    valueRange: IntRange,
    label: String,
    valueLabel: (Int) -> String = { it.toString() },
    onValueChange: (Int) -> Unit
) {
    var currentValue by remember { mutableStateOf(defaultValue) }

    Row(
        Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(INT_SELECTOR_WIDTH_FACTOR),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.h6
            )
            Text(
                text = valueLabel(currentValue),
                style = MaterialTheme.typography.h6
            )
        }

        Slider(
            value = currentValue.toFloat(),
            onValueChange = {
                currentValue = it.toInt()
                onValueChange(currentValue)
            },
            valueRange = valueRange.first.toFloat()..valueRange.last.toFloat()
        )
    }
}
