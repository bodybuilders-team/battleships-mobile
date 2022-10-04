package pt.isel.pdm.battleships.ui.screens.gameplay.board

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import pt.isel.pdm.battleships.domain.board.Coordinate
import pt.isel.pdm.battleships.ui.theme.DarkBlue

const val DEFAULT_TILE_SIZE = 32.0f

/**
 * Visual representation of a board tile.
 *
 * @param size the size of the tile
 * @param coordinate the coordinate of the tile
 * @param onTileClicked callback to be invoked when the tile is clicked
 */
@Composable
fun TileView(
    size: Float,
    coordinate: Coordinate,
    onTileClicked: ((Coordinate) -> Unit)? = null
) {
    Box(
        Modifier
            .size(size.dp)
            .background(DarkBlue)
            .border((size / DEFAULT_TILE_SIZE).dp, Color.LightGray)
            .clickable(enabled = onTileClicked != null) {
                if (onTileClicked != null) onTileClicked(coordinate)
            }
    )
}
