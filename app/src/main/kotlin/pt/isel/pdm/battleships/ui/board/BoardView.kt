package pt.isel.pdm.battleships.ui.board

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import pt.isel.pdm.battleships.domain.board.Board
import pt.isel.pdm.battleships.ui.ship.ShipView
import pt.isel.pdm.battleships.ui.theme.DarkBlue

const val TILE_SIZE = 35.0f
private const val TILE_BORDER_SIZE = 1

/**
 * The view that shows the board of the game.
 *
 * @param board the board to be shown
 */
@Composable
fun BoardView(board: Board) {
    Box {
        Column {
            repeat(Board.BOARD_SIDE_LENGTH) {
                Row {
                    repeat(Board.BOARD_SIDE_LENGTH) {
                        Box(
                            Modifier
                                .size(TILE_SIZE.dp)
                                .background(DarkBlue)
                                .border(TILE_BORDER_SIZE.dp, Color.LightGray)
                        )
                    }
                }
            }
        }

        for (ship in board.fleet)
            ShipView(ship)
    }
}
