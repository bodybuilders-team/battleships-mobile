package pt.isel.pdm.battleships.ui.screens.gameplay.boardSetup.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import pt.isel.pdm.battleships.domain.games.ship.Ship
import pt.isel.pdm.battleships.ui.screens.gameplay.shared.ship.toPoint

/**
 * A draggable ship already placed on the board.
 *
 * @param ship the ship
 * @param tileSize the size of the tile
 * @param hide whether to hide the ship
 * @param onDragStart the callback to be invoked when the drag starts
 * @param onDragEnd the callback to be invoked when the drag ends
 * @param onDragCancel the callback to be invoked when the drag is cancelled
 * @param onDrag the callback to be invoked when the ship is dragged
 * @param onTap the callback to be invoked when the ship is tapped
 */
@Composable
fun PlacedDraggableShipView(
    ship: Ship,
    tileSize: Float,
    hide: Boolean,
    onDragStart: (Ship, Offset) -> Unit,
    onDragEnd: (Ship) -> Unit,
    onDragCancel: () -> Unit,
    onDrag: (Offset) -> Unit,
    onTap: () -> Unit
) {
    val (xPoint, yPoint) = ship.coordinate.toPoint()

    Box(
        modifier = Modifier
            .offset(
                x = (xPoint * tileSize).dp,
                y = (yPoint * tileSize).dp
            )
            .alpha(if (hide) 0f else 1f)
    ) {
        DraggableShipView(
            ship = ship,
            tileSize = tileSize,
            onDragStart = onDragStart,
            onDragEnd = onDragEnd,
            onDragCancel = onDragCancel,
            onDrag = onDrag,
            onTap = onTap
        )
    }
}
