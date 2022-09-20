package pt.isel.pdm.battleships.ui.screens.gameplay.board

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import pt.isel.pdm.battleships.domain.board.Board
import pt.isel.pdm.battleships.domain.board.Board.Companion.BOARD_SIDE_LENGTH
import pt.isel.pdm.battleships.domain.board.Coordinate
import pt.isel.pdm.battleships.ui.screens.gameplay.ship.ShipView
import pt.isel.pdm.battleships.ui.theme.DarkBlue

const val TILE_SIZE = 32.0f
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
            ColumnsIdentifierView()
            repeat(BOARD_SIDE_LENGTH) {
                Row {
                    RowIdentifierBox(it)
                    repeat(BOARD_SIDE_LENGTH) {
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
            ShipView(ship, Offset(x = TILE_SIZE, y = TILE_SIZE))
    }
}

/**
 * Composable used to display board column letters.
 */
@Composable
private fun ColumnsIdentifierView() {
    Row(modifier = Modifier.offset(x = TILE_SIZE.dp)) {
        repeat(BOARD_SIDE_LENGTH) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(TILE_SIZE.dp)
            ) {
                Text(
                    "${Coordinate.COLS_RANGE.first + it}"
                )
            }
        }
    }
}

/**
 * Composable used to display board row number.
 *
 * @param row the row number to be displayed
 */
@Composable
private fun RowIdentifierBox(row: Int) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(TILE_SIZE.dp)
    ) {
        Text("${row + 1}")
    }
}
