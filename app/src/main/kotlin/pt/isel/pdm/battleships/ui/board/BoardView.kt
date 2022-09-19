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
import pt.isel.pdm.battleships.ui.ShipView

const val TILE_SIZE = 35.0f

/**
 * The view that shows the board of the game.
 */
@Composable
fun BoardView(board: Board) {
    Box {
        Column {
            repeat(Board.BOARD_SIDE_LENGTH) { y ->
                Row {
                    repeat(Board.BOARD_SIDE_LENGTH) { x ->
                        Column {
                            Box(
                                Modifier
                                    .size(TILE_SIZE.dp)
                                    .background(Color.Blue)
                                    .border(1.dp, Color.Cyan)
                            ) {
                            }
                        }
                    }
                }
            }
        }

        for (ship in board.fleet)
            ShipView(ship)
    }
}
