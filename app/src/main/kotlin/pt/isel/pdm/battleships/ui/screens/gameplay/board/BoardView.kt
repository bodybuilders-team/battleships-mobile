package pt.isel.pdm.battleships.ui.screens.gameplay.board

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import pt.isel.pdm.battleships.domain.board.Board
import pt.isel.pdm.battleships.domain.board.Board.Companion.FIRST_COL
import pt.isel.pdm.battleships.domain.board.Board.Companion.FIRST_ROW
import pt.isel.pdm.battleships.domain.board.Coordinate
import pt.isel.pdm.battleships.ui.screens.gameplay.ship.toPoint

private const val BOARD_VIEW_BOX_SIZE = 320.0f
private const val SELECTED_TILE_BORDER_SIZE = 2
const val FULL_BOARD_VIEW_BOX_SIZE = BOARD_VIEW_BOX_SIZE + DEFAULT_TILE_SIZE
const val DEFAULT_TILE_SIZE_FACTOR = 1.0f

/**
 * The view that shows the board of the game.
 *
 * @param board the board to be shown
 * @param onTileClicked callback to be invoked when a tile is clicked
 * @param tileSizeFactor the factor by which the tile size is multiplied
 */
@Composable
fun BoardView(
    board: Board,
    onTileClicked: ((Coordinate) -> Unit)?,
    tileSizeFactor: Float = DEFAULT_TILE_SIZE_FACTOR
) {
    val tileSize = getTileSize(board.size) * tileSizeFactor

    Box {
        Column {
            repeat(board.size) { rowIdx ->
                Row {
                    repeat(board.size) { colIdx ->
                        TileView(
                            size = tileSize,
                            coordinate = Coordinate(
                                col = FIRST_COL + colIdx,
                                row = FIRST_ROW + rowIdx
                            ),
                            onTileClicked = onTileClicked
                        )
                    }
                }
            }
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
            .border(SELECTED_TILE_BORDER_SIZE.dp, color = Color.Green)
    )
}

/**
 * Gets the tile size based on the board size.
 *
 * @param boardSize the size of the board
 * @return the tile size
 */
fun getTileSize(boardSize: Int) = FULL_BOARD_VIEW_BOX_SIZE / (boardSize + 1)
