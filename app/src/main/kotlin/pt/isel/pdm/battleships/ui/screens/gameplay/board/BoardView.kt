package pt.isel.pdm.battleships.ui.screens.gameplay.board

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.isel.pdm.battleships.domain.board.Board
import pt.isel.pdm.battleships.domain.board.Board.Companion.FIRST_COL
import pt.isel.pdm.battleships.domain.board.Board.Companion.FIRST_ROW
import pt.isel.pdm.battleships.domain.board.Coordinate
import pt.isel.pdm.battleships.ui.screens.gameplay.ship.PlacedShipView
import pt.isel.pdm.battleships.ui.screens.gameplay.ship.toPoint

const val BOARD_VIEW_BOX_SIZE = 320.0f
const val FULL_BOARD_VIEW_BOX_SIZE = BOARD_VIEW_BOX_SIZE + DEFAULT_TILE_SIZE

const val COL_IDENTIFIER_FONT_SIZE_FACTOR = 0.4f

/**
 * The view that shows the board of the game with column and row identifiers.
 *
 * @param board the board to be shown
 * @param selectedCells the cells that are currently selected
 * @param tileSizeFactor the factor by which the tile size is multiplied
 * @param onTileClicked callback to be invoked when a tile is clicked
 */
@Composable
fun BoardViewWithIdentifiers(
    board: Board,
    selectedCells: List<Coordinate>,
    onTileClicked: ((Coordinate) -> Unit)?,
    tileSizeFactor: Float = 1.0f
) {
    IdentifiersWrapper(boardSize = board.size, tileSizeFactor = tileSizeFactor) {
        BoardView(
            board = board,
            selectedCells = selectedCells,
            tileSizeFactor = tileSizeFactor,
            onTileClicked = onTileClicked
        )
    }
}

/**
 * The view that shows the board of the game.
 *
 * @param board the board to be shown
 * @param selectedCells the cells that are currently selected
 * @param onTileClicked callback to be invoked when a tile is clicked
 * @param tileSizeFactor the factor by which the tile size is multiplied
 */
@Composable
fun BoardView(
    board: Board,
    selectedCells: List<Coordinate>,
    onTileClicked: ((Coordinate) -> Unit)?,
    tileSizeFactor: Float = 1.0f
) {
    val tileSize = getTileSize(board.size) * tileSizeFactor

    Box {
        Column {
            repeat(board.size) { rowIdx ->
                Row {
                    repeat(board.size) { colIdx ->
                        val coordinate = Coordinate(
                            col = FIRST_COL + colIdx,
                            row = FIRST_ROW + rowIdx
                        )
                        TileView(
                            size = tileSize,
                            coordinate = coordinate,
                            onTileClicked = onTileClicked
                        )
                    }
                }
            }
        }
        board.fleet.forEach { ship ->
            PlacedShipView(ship, tileSize)
        }
        selectedCells.forEach {
            TileSelectionView(it, tileSize)
        }
    }
}

/**
 * The view that shows the tile selection (on top of the tile).
 *
 * @param coordinate the coordinate of the tile
 * @param tileSize the size of the tile
 */
@Composable
fun TileSelectionView(coordinate: Coordinate, tileSize: Float) {
    val (xPoint, yPoint) = coordinate.toPoint()

    Box(
        modifier = Modifier
            .offset(
                x = (xPoint * tileSize).dp,
                y = (yPoint * tileSize).dp
            )
            .size(tileSize.dp)
            .border(2.dp, color = Color.Green)
    )
}

/**
 * The view that shows the column and row identifiers.
 *
 * @param boardSize the size of the board
 * @param tileSizeFactor the factor by which the tile size is multiplied
 * @param content the content to be shown
 */
@Composable
fun IdentifiersWrapper(
    boardSize: Int,
    tileSizeFactor: Float = 1.0f,
    content: @Composable () -> Unit
) {
    Box {
        Column {
            ColumnsIdentifierView(boardSize, tileSizeFactor)
            Row {
                RowsIdentifierView(boardSize, tileSizeFactor)
                Box {
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
private fun ColumnsIdentifierView(boardSize: Int, tileSizeFactor: Float = 1.0f) {
    val tileSize = getTileSize(boardSize) * tileSizeFactor

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
 * @param tileSizeFactor the factor by which the tile size is multiplied
 */
@Composable
private fun RowsIdentifierView(boardSize: Int, tileSizeFactor: Float = 1.0f) {
    val tileSize = getTileSize(boardSize) * tileSizeFactor

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
