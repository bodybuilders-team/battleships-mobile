package pt.isel.pdm.battleships.ui

import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import java.security.InvalidParameterException
import kotlin.math.roundToInt
import pt.isel.pdm.battleships.domain.board.Board
import pt.isel.pdm.battleships.domain.board.Coordinate
import pt.isel.pdm.battleships.domain.ship.Orientation
import pt.isel.pdm.battleships.ui.board.TILE_SIZE

fun Coordinate.Companion.fromPoint(col: Int, row: Int): Coordinate {
    if (col !in 0 until Board.BOARD_SIDE_LENGTH) {
        throw InvalidParameterException("Invalid Coordinate: x out of range")
    }
    if (row !in 0 until Board.BOARD_SIDE_LENGTH) {
        throw InvalidParameterException("Invalid Coordinate: y out of range")
    }

    return Coordinate(COLS_RANGE.toList()[col], row + 1)
}

fun Coordinate.toPoint(): Pair<Int, Int> {
    return Pair(col - Coordinate.COLS_RANGE.first, row - 1)
}

@Composable
fun UnplacedShipView(
    orientation: Orientation = Orientation.HORIZONTAL,
    initialOffset: Offset = Offset.Zero,
    size: Int,
    onShipPlacedCallback: (Coordinate) -> Boolean
) {
    val currentSize by rememberUpdatedState(size)
    val currentOrientation by rememberUpdatedState(orientation)

    var dragging by remember {
        mutableStateOf(false)
    }
    var dragOffset by remember {
        mutableStateOf(Offset.Zero)
    }

    val d = LocalDensity.current

    fun getOffset(): Offset = Offset(
        initialOffset.x + (
            if (dragging) dragOffset.x else 0f
            ) / d.density,
        initialOffset.y + (
            if (dragging) dragOffset.y else 0f
            ) / d.density
    )

    val offset by rememberUpdatedState(getOffset())

    Box(
        Modifier
            .offset(
                offset.x.dp,
                offset.y.dp
            )
            .size(
                (TILE_SIZE * if (orientation == Orientation.HORIZONTAL) size else 1).dp,
                (TILE_SIZE * if (orientation == Orientation.VERTICAL) size else 1).dp
            )
            .background(Color.Black)
            .pointerInput(Unit) {
                this.detectDragGestures(
                    onDragStart = { dragging = true },
                    onDragEnd = {
                        dragging = false
                        val currCol = (offset.x / TILE_SIZE).roundToInt()
                        val currRow = (offset.y / TILE_SIZE).roundToInt()

                        if ((
                            currentOrientation == Orientation.HORIZONTAL &&
                                (currCol until currCol + currentSize).all {
                                    it in 0 until Board.BOARD_SIDE_LENGTH
                                } &&
                                currRow in 0 until Board.BOARD_SIDE_LENGTH
                            ) || (
                                currentOrientation == Orientation.VERTICAL &&
                                    (currRow until currRow + currentSize).all {
                                        it in 0 until Board.BOARD_SIDE_LENGTH
                                    } &&
                                    currCol in 0 until Board.BOARD_SIDE_LENGTH
                                )
                        ) {
                            onShipPlacedCallback(
                                Coordinate.fromPoint(currCol, currRow)
                            )
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
    }
}
