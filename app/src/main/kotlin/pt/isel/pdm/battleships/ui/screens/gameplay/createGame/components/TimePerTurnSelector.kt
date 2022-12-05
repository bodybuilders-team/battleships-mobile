package pt.isel.pdm.battleships.ui.screens.gameplay.createGame.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import pt.isel.pdm.battleships.R

private const val MIN_TIME_PER_TURN = 10 // Seconds
private const val MAX_TIME_PER_TURN = 120 // Seconds

/**
 * Selector that allows the user to choose the time that each player has to do their turn.
 *
 * @param timePerTurn the current time that each player has to do their turn
 * @param onValueChange callback that is called when the user changes the time
 */
@Composable
fun TimePerTurnSelector(timePerTurn: Int, onValueChange: (Int) -> Unit) {
    IntSelector(
        defaultValue = timePerTurn,
        valueRange = MIN_TIME_PER_TURN..MAX_TIME_PER_TURN,
        label = stringResource(R.string.gameConfig_timePerRound_text),
        valueLabel = { "$it s" },
        onValueChange = onValueChange
    )
}
