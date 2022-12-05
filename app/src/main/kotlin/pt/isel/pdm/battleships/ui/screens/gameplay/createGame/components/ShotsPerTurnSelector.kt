package pt.isel.pdm.battleships.ui.screens.gameplay.createGame.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import pt.isel.pdm.battleships.R

private const val MIN_SHOTS_PER_TURN = 1
private const val MAX_SHOTS_PER_TURN = 5

/**
 * Selector that allows the user to choose the number of shots that each player can do per turn.
 *
 * @param shotsPerTurn the current number of shots that each player can do per turn
 * @param onValueChange callback that is called when the user changes the number of shots
 */
@Composable
fun ShotsPerTurnSelector(shotsPerTurn: Int, onValueChange: (Int) -> Unit) {
    IntSelector(
        defaultValue = shotsPerTurn,
        valueRange = MIN_SHOTS_PER_TURN..MAX_SHOTS_PER_TURN,
        label = stringResource(R.string.gameConfig_shotsPerTurn_text),
        onValueChange = onValueChange
    )
}
