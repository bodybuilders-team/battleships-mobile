package pt.isel.pdm.battleships.ui.screens.gameplay.shared.board

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
import pt.isel.pdm.battleships.domain.games.board.Board

private const val COL_IDENTIFIER_FONT_SIZE_FACTOR = 0.4f

/**
 * The view that shows the column and row identifiers, wrapping the board and other content in a
 * box, allowing for the board cells to be overridden.
 *
 * @param board the board to be shown
 * @param tileSizeFactor the factor by which the tile size is multiplied
 * @param content the content to be shown in the box alongside the board
 */
@Composable
fun BoardViewWithIdentifiers(
    board: Board,
    tileSizeFactor: Float = DEFAULT_TILE_SIZE_FACTOR,
    content: @Composable () -> Unit
) {
    Box {
        Column {
            ColumnsIdentifierView(board.size, tileSizeFactor)
            Row {
                RowsIdentifierView(board.size, tileSizeFactor)
                Box {
                    BoardView(
                        board = board,
                        tileSizeFactor = tileSizeFactor
                    )
                    content()
                }
            }
        }
    }
}

/**
 * Composable used to display board column letters.
 *
 * @param boardSize the size of the board
 * @param tileSizeFactor the factor by which the tile size is multiplied
 */
@Composable
private fun ColumnsIdentifierView(
    boardSize: Int,
    tileSizeFactor: Float = DEFAULT_TILE_SIZE_FACTOR
) {
    val tileSize = getTileSize(boardSize) * tileSizeFactor

    Row(modifier = Modifier.offset(x = tileSize.dp)) {
        repeat(boardSize) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(tileSize.dp)
            ) {
                Text(
                    text = "${Board.FIRST_COL + it}",
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
 * @param tileSizeFactor the factor by which the tile size is multiplied
 */
@Composable
private fun RowsIdentifierView(
    boardSize: Int,
    tileSizeFactor: Float = DEFAULT_TILE_SIZE_FACTOR
) {
    val tileSize = getTileSize(boardSize) * tileSizeFactor

    Column {
        repeat(boardSize) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(tileSize.dp)
            ) {
                Text(
                    text = "${Board.FIRST_ROW + it}",
                    fontSize = tileSize.sp * COL_IDENTIFIER_FONT_SIZE_FACTOR
                )
            }
        }
    }
}
