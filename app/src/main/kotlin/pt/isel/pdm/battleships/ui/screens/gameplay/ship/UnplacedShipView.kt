package pt.isel.pdm.battleships.ui.screens.gameplay.ship

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import pt.isel.pdm.battleships.domain.board.Board.Companion.FIRST_COL
import pt.isel.pdm.battleships.domain.board.Coordinate
import pt.isel.pdm.battleships.domain.ship.Orientation
import pt.isel.pdm.battleships.domain.ship.Ship
import pt.isel.pdm.battleships.domain.ship.ShipType
import pt.isel.pdm.battleships.ui.screens.gameplay.board.getTileSize
import kotlin.math.roundToInt

/**
 * A composable that represents a ship not yet placed on the board.
 *
 * @param orientation the orientation of the ship
 * @param initialOffset the initial offset of the ship
 * @param boardOffset the offset of the board
 * @param type the type of the ship
 * @param boardSize the size of the board
 * @param onShipPlaced what to do when the ship is placed on the board
 */
@Composable
fun UnplacedShipView(
    orientation: Orientation = Orientation.HORIZONTAL,
    initialOffset: Offset = Offset.Zero,
    boardOffset: Offset = Offset.Zero,
    type: ShipType,
    boardSize: Int,
    onShipPlaced: (Coordinate) -> Boolean
) {
    val currentSize by rememberUpdatedState(type.size)
    val currentOrientation by rememberUpdatedState(orientation)

    var dragging by remember { mutableStateOf(false) }
    var dragOffset by remember { mutableStateOf(Offset.Zero) }

    val tileSize = getTileSize(boardSize)

    val d = LocalDensity.current

    /**
     * Calculates the offset of the ship based on the current drag offset and the initial offset.
     *
     * @return the offset
     */
    fun getOffset(): Offset = Offset(
        x = initialOffset.x + (if (dragging) dragOffset.x else 0f) / d.density,
        y = initialOffset.y + (if (dragging) dragOffset.y else 0f) / d.density
    )

    val offset by rememberUpdatedState(getOffset())

    Box(
        Modifier
            .offset(
                x = offset.x.dp,
                y = offset.y.dp
            )
            .size(
                width = (tileSize * if (orientation == Orientation.HORIZONTAL) type.size else 1).dp,
                height = (tileSize * if (orientation == Orientation.VERTICAL) type.size else 1).dp
            )
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { dragging = true },
                    onDragEnd = {
                        dragging = false
                        val currCol = ((offset.x - boardOffset.x) / tileSize).roundToInt()
                        val currRow = ((offset.y - boardOffset.y) / tileSize).roundToInt()

                        Coordinate
                            .fromPointOrNull(currCol, currRow)
                            ?.let { coordinate ->
                                if (
                                    Ship.isValidShipCoordinate(
                                        coordinate,
                                        currentOrientation,
                                        currentSize,
                                        boardSize
                                    )
                                ) {
                                    onShipPlaced(coordinate)
                                }
                            }

                        dragOffset = Offset.Zero
                    },
                    onDragCancel = {
                        dragging = false
                        dragOffset = Offset.Zero
                    }
                ) { change, dragAmount ->
                    change.consumeAllChanges()
                    dragOffset += dragAmount
                }
            }
    ) {
        ShipImage(
            type = type,
            orientation = orientation
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
fun Coordinate.Companion.fromPoint(col: Int, row: Int) = Coordinate(FIRST_COL + col, row + 1)

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
        isValid(FIRST_COL + col, row + 1) -> fromPoint(col, row)
        else -> null
    }
}

/**
 * Converts a Coordinate to a point.
 *
 * @return a pair of integers representing the point
 */
fun Coordinate.toPoint(): Pair<Int, Int> = Pair(col - FIRST_COL, row - 1)
