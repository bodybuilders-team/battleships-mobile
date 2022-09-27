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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.isel.pdm.battleships.domain.board.Board
import pt.isel.pdm.battleships.domain.board.Board.Companion.FIRST_COL
import pt.isel.pdm.battleships.domain.board.Board.Companion.FIRST_ROW
import pt.isel.pdm.battleships.ui.screens.gameplay.ship.PlacedShipView

const val BOARD_VIEW_BOX_SIZE = 320.0f
const val FULL_BOARD_VIEW_BOX_SIZE = BOARD_VIEW_BOX_SIZE + DEFAULT_TILE_SIZE

const val COL_IDENTIFIER_FONT_SIZE_FACTOR = 0.4f

/**
 * The view that shows the board of the game with column and row identifiers.
 *
 * @param board the board to be shown
 */
@Composable
fun BoardViewWithIdentifiers(board: Board) {
    IdentifiersWrapper(boardSize = board.size) {
        BoardView(board = board)
    }
}

/**
 * The view that shows the column and row identifiers.
 *
 * @param boardSize the size of the board
 * @param content the content to be shown
 */
@Composable
fun IdentifiersWrapper(boardSize: Int, content: @Composable () -> Unit) {
    Box {
        Column {
            ColumnsIdentifierView(boardSize)
            Row {
                RowsIdentifierView(boardSize)
                Box {
                    content()
                }
            }
        }
    }
}

/**
 * The view that shows the board of the game.
 *
 * @param board the board to be shown
 */
@Composable
fun BoardView(board: Board) {
    val tileSize = getTileSize(board.size)

    Box {
        Column {
            repeat(board.size) {
                Row {
                    repeat(board.size) {
                        TileView(tileSize)
                    }
                }
            }
        }
        board.fleet.forEach { ship ->
            PlacedShipView(ship, tileSize)
        }
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
 * Composable used to display board column letters.
 *
 * @param boardSize the size of the board
 */
@Composable
private fun RowsIdentifierView(boardSize: Int) {
    val tileSize = getTileSize(boardSize)

    Column {
        repeat(boardSize) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(tileSize.dp)
            ) {
                Text(
                    text = "${FIRST_ROW + it}",
                    fontSize = tileSize.sp * COL_IDENTIFIER_FONT_SIZE_FACTOR
                )
            }
        }
    }
}

/**
 * Gets the tile size based on the board size.
 *
 * @param boardSize the size of the board
 */
fun getTileSize(boardSize: Int) = FULL_BOARD_VIEW_BOX_SIZE / (boardSize + 1)
