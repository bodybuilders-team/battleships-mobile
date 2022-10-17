package pt.isel.pdm.battleships.ui.screens.gameplay.newGame.boardSetup.shipPlacing

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import pt.isel.pdm.battleships.domain.ship.Ship
import pt.isel.pdm.battleships.ui.screens.gameplay.ship.ShipView

/**
 * Represents a draggable ship.
 *
 * @param ship the ship
 * @param tileSize the size of the tile
 * @param onDragStart the callback to be invoked when the drag starts
 * @param onDragEnd the callback to be invoked when the drag ends
 * @param onDragCancel the callback to be invoked when the drag is cancelled
 * @param onDrag the callback to be invoked when the ship is dragged
 * @param onTap the callback to be invoked when the ship is tapped
 * @param modifier the modifier
 */
@Composable
fun DraggableShipView(
    ship: Ship,
    tileSize: Float,
    onDragStart: (Ship, Offset) -> Unit,
    onDragEnd: (Ship) -> Unit,
    onDragCancel: () -> Unit,
    onDrag: (Offset) -> Unit,
    onTap: () -> Unit,
    modifier: Modifier = Modifier
) {
    var initialPosition by remember { mutableStateOf(Offset(0f, 0f)) }

    Box(
        modifier = Modifier
            .onGloballyPositioned { initialPosition = it.positionInWindow() }
            .pointerInput(ship) {
                detectDragGestures(
                    onDragStart = { onDragStart(ship, initialPosition) },
                    onDragEnd = { onDragEnd(ship) },
                    onDragCancel = { onDragCancel() },
                    onDrag = { change, dragAmount ->
                        change.consumeAllChanges()
                        onDrag(dragAmount)
                    }
                )
            }
            .pointerInput(ship) {
                detectTapGestures(
                    onTap = { onTap() }
                )
            }
    ) {
        ShipView(
            type = ship.type,
            orientation = ship.orientation,
            tileSize = tileSize,
            modifier = modifier
        )
    }
}
