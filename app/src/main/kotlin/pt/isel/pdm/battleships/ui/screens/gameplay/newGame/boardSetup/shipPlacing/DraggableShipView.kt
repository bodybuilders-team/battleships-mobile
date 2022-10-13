package pt.isel.pdm.battleships.ui.screens.gameplay.newGame.boardSetup.shipPlacing

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import pt.isel.pdm.battleships.domain.board.Board
import pt.isel.pdm.battleships.domain.board.Coordinate
import pt.isel.pdm.battleships.domain.ship.Orientation
import pt.isel.pdm.battleships.domain.ship.ShipType
import pt.isel.pdm.battleships.ui.screens.gameplay.newGame.boardSetup.DragState
import pt.isel.pdm.battleships.ui.screens.gameplay.ship.ShipView

@Composable
fun DraggableShipView(
    type: ShipType,
    orientation: Orientation,
    tileSize: Float,
    dragState: DragState,
    onDragStart: () -> Unit,
    onDragEnd: (ShipType) -> Unit,
    onTap: (ShipType) -> Unit,
    modifier: Modifier = Modifier
) {
    var initialPosition by remember { mutableStateOf(Offset(0f, 0f)) }

    val currentOrientation by rememberUpdatedState(newValue = orientation)

    Box(
        modifier = Modifier
            .onGloballyPositioned {
                initialPosition = it.positionInWindow()
            }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = {
                        onDragStart()
                        dragState.shipType = type
                        dragState.shipOrientation = currentOrientation
                        dragState.isDragging = true
                        dragState.initialOffset = initialPosition
                    },
                    onDragEnd = {
                        onDragEnd(type)
                        dragState.isDragging = false
                        dragState.dragOffset = Offset.Zero
                    },
                    onDragCancel = {
                        dragState.isDragging = false
                        dragState.dragOffset = Offset.Zero
                    },
                    onDrag = { change, dragAmount ->
                        change.consumeAllChanges()
                        dragState.dragOffset += dragAmount
                    }
                )
            }
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        onTap(type)
                    }
                )
            }
    ) {
        ShipView(
            type = type,
            orientation = orientation,
            tileSize = tileSize,
            modifier = modifier
        )
    }
}

/**
 * Gets a coordinate from a point.
 *
 * @param col the column of the point
 * @param row the row of the point
 *
 * @return the coordinate
 */
private fun Coordinate.Companion.fromPoint(col: Int, row: Int) =
    Coordinate(Board.FIRST_COL + col, row + 1)

/**
 * Gets a coordinate from a point or null if it isn't valid.
 *
 * @param col the column of the point
 * @param row the row of the point
 *
 * @return the coordinate or null if it isn't valid
 */
fun Coordinate.Companion.fromPointOrNull(col: Int, row: Int): Coordinate? {
    return when {
        isValid(Board.FIRST_COL + col, row + 1) -> fromPoint(col, row)
        else -> null
    }
}
