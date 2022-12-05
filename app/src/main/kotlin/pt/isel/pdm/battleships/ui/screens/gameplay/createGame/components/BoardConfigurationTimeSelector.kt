package pt.isel.pdm.battleships.ui.screens.gameplay.createGame.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import pt.isel.pdm.battleships.R

private const val MIN_TIME_FOR_BOARD_CONFIG = 10 // Seconds
private const val MAX_TIME_FOR_BOARD_CONFIG = 120 // Seconds

/**
 * Selector that allows the user to choose the time that the players have to configure their boards.
 *
 * @param timeForLayoutPhase the current time that the players have to configure their boards
 * @param onValueChange callback that is called when the user changes the time
 * that the players have to configure their boards
 */
@Composable
fun BoardConfigurationTimeSelector(timeForLayoutPhase: Int, onValueChange: (Int) -> Unit) {
    IntSelector(
        defaultValue = timeForLayoutPhase,
        valueRange = MIN_TIME_FOR_BOARD_CONFIG..MAX_TIME_FOR_BOARD_CONFIG,
        label = stringResource(R.string.gameConfig_timeForGridLayout_text),
        valueLabel = { "$it s" },
        onValueChange = onValueChange
    )
}
