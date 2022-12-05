package pt.isel.pdm.battleships.ui.screens.gameplay.createGame.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import pt.isel.pdm.battleships.R
import pt.isel.pdm.battleships.domain.games.board.Board

/**
 * Selector that allows the user to choose the size of the board.
 *
 * @param boardSize the current size of the board
 * @param onValueChange callback that is called when the user changes the size of the board
 */
@Composable
fun GridSizeSelector(boardSize: Int, onValueChange: (Int) -> Unit) {
    IntSelector(
        defaultValue = boardSize,
        valueRange = Board.MIN_BOARD_SIZE..Board.MAX_BOARD_SIZE,
        label = stringResource(R.string.gameConfig_gridSize_text),
        valueLabel = { "$it x $it" },
        onValueChange = onValueChange
    )
}
