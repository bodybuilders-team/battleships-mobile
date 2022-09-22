package pt.isel.pdm.battleships.ui.screens.gameplay.board

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.isel.pdm.battleships.domain.board.Board
import pt.isel.pdm.battleships.domain.board.Board.Companion.FIRST_COL
import pt.isel.pdm.battleships.ui.screens.gameplay.ship.ShipView

const val BOARD_VIEW_BOX_SIZE = 320.0f
const val FULL_BOARD_VIEW_BOX_SIZE = BOARD_VIEW_BOX_SIZE + DEFAULT_TILE_SIZE

const val COL_IDENTIFIER_FONT_SIZE_FACTOR = 0.4f

/**
 * The view that shows the board of the game.
 *
 * @param board the board to be shown
 */
@Composable
fun BoardView(board: Board) {
    val tileSize = getTileSize(board.size)

    Box(modifier = Modifier.size(FULL_BOARD_VIEW_BOX_SIZE.dp)) {
        Column {
            ColumnsIdentifierView(board.size)
            repeat(board.size) {
                Row {
                    RowIdentifierBox(it, tileSize)
                    repeat(board.size) {
                        TileView(tileSize)
                    }
                }
            }
        }

        for (ship in board.fleet)
            ShipView(ship, tileSize, Offset(x = tileSize, y = tileSize))
    }
}

/**
 * Composable used to display board column letters.
 *
 * @param boardSize the size of the board
 */
@Composable
private fun ColumnsIdentifierView(boardSize: Int) {
    val tileSize = getTileSize(boardSize)

    Row(modifier = Modifier.offset(x = tileSize.dp)) {
        repeat(boardSize) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(tileSize.dp)
            ) {
                Text(
                    text = "${FIRST_COL + it}",
                    fontSize = tileSize.sp * COL_IDENTIFIER_FONT_SIZE_FACTOR
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
private fun RowIdentifierBox(row: Int, tileSize: Float) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(tileSize.dp)
    ) {
        Text(
            text = "${row + 1}",
            fontSize = tileSize.sp * COL_IDENTIFIER_FONT_SIZE_FACTOR
        )
    }
}

/**
 * Gets the tile size based on the board size.
 *
 * @param boardSize the size of the board
 */
fun getTileSize(boardSize: Int) = FULL_BOARD_VIEW_BOX_SIZE / (boardSize + 1)
