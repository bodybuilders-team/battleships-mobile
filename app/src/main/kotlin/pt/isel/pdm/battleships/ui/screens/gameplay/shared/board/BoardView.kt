package pt.isel.pdm.battleships.ui.screens.gameplay.shared.board

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import pt.isel.pdm.battleships.domain.games.board.Board

private const val BOARD_VIEW_BOX_SIZE = 320.0f
const val SELECTED_TILE_BORDER_SIZE = 2
const val FULL_BOARD_VIEW_BOX_SIZE = BOARD_VIEW_BOX_SIZE + DEFAULT_TILE_SIZE
const val DEFAULT_TILE_SIZE_FACTOR = 1.0f

/**
 * The view that shows the board of the game.
 *
 * @param board the board to be shown
 * @param tileSizeFactor the factor by which the tile size is multiplied
 */
@Composable
fun BoardView(
    board: Board,
    tileSizeFactor: Float = DEFAULT_TILE_SIZE_FACTOR
) {
    val tileSize = getTileSize(board.size) * tileSizeFactor

    Box {
        Column {
            repeat(board.size) {
                Row {
                    repeat(board.size) {
                        TileView(size = tileSize)
                    }
                }
            }
        }
    }
}

/**
 * The view that shows the tile selection overlay (on top of the tile).
 *
 * @param tileSize the size of the tile
 * @param selectionEnabled if the tile selection is enabled
 * @param selected if the tile is selected
 * @param onTileClicked callback to be invoked when a tile is clicked
 */
@Composable
fun TileSelectionView(
    tileSize: Float,
    selectionEnabled: Boolean,
    selected: Boolean,
    onTileClicked: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(tileSize.dp)
            .clickable(enabled = selectionEnabled) { onTileClicked() }
            .then(
                if (selected) {
                    Modifier.border(
                        width = SELECTED_TILE_BORDER_SIZE.dp,
                        color = Color.Green
                    )
                } else Modifier
            )
    )
}

/**
 * The view that shows the tile hit overlay (on top of the tile).
 *
 * @param tileSize the size of the tile
 * @param hitShip if the tile was hit
 */
@Composable
fun TileHitView(
    tileSize: Float,
    hitShip: Boolean
) {
    val density = LocalDensity.current
    val borderOffset = 2 * density.density

    Box(modifier = Modifier.size(tileSize.dp)) {
        Canvas(
            modifier = Modifier.size(tileSize.dp),
            onDraw = {
                drawLine(
                    color = if (hitShip) Color.Red else Color.Gray,
                    start = Offset(
                        x = tileSize * density.density - borderOffset,
                        y = borderOffset
                    ),
                    end = Offset(
                        x = borderOffset,
                        y = tileSize * density.density - borderOffset
                    ),
                    strokeWidth = 5f
                )
                drawLine(
                    color = if (hitShip) Color.Red else Color.Gray,
                    start = Offset(
                        x = tileSize * density.density - borderOffset,
                        y = tileSize * density.density - borderOffset
                    ),
                    end = Offset(
                        x = borderOffset,
                        y = borderOffset
                    ),
                    strokeWidth = 5f
                )
            }
        )
    }
}

/**
 * Gets the tile size based on the board size.
 *
 * @param boardSize the size of the board
 * @return the tile size
 */
fun getTileSize(boardSize: Int) = FULL_BOARD_VIEW_BOX_SIZE / (boardSize + 1)
