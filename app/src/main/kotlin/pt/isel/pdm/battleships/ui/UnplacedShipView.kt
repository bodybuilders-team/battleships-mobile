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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import pt.isel.pdm.battleships.domain.Board
import pt.isel.pdm.battleships.domain.Coordinate
import pt.isel.pdm.battleships.domain.Orientation
import java.security.InvalidParameterException
import kotlin.math.roundToInt

fun Coordinate.Companion.fromPoint(col: Int, row: Int): Coordinate {
    if (col !in 0 until Board.BOARD_SIDE_LENGTH)
        throw InvalidParameterException("Invalid Coordinate: x out of range")
    if (row !in 0 until Board.BOARD_SIDE_LENGTH)
        throw InvalidParameterException("Invalid Coordinate: y out of range")

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
    onShipPlacedCallback: (Coordinate) -> Boolean,
) {

    var dragging by remember {
        mutableStateOf(false)
    }
    var dragOffset by remember {
        mutableStateOf(Offset.Zero)
    }

    val d = LocalDensity.current

    fun getOffset() = Offset(
        initialOffset.x + (if (dragging)
            dragOffset.x
        else
            0f) / d.density,
        initialOffset.y + (if (dragging)
            dragOffset.y
        else
            0f) / d.density

    )

    val offset = getOffset()

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
                        val currOffset = getOffset()

                        dragging = false
                        val currCol = (currOffset.x / TILE_SIZE).roundToInt()
                        val currRow = (currOffset.y / TILE_SIZE).roundToInt()

                        if ((orientation == Orientation.HORIZONTAL &&
                                    (currCol until currCol + size).all {
                                        it in 0 until Board.BOARD_SIDE_LENGTH
                                    } &&
                                    currRow in 0 until Board.BOARD_SIDE_LENGTH)
                            ||
                            (orientation == Orientation.VERTICAL &&
                                    (currRow until currRow + size).all {
                                        it in 0 until Board.BOARD_SIDE_LENGTH
                                    } &&
                                    currCol in 0 until Board.BOARD_SIDE_LENGTH)
                        )
                            onShipPlacedCallback(
                                Coordinate.fromPoint(currCol, currRow)
                            )

                        dragOffset = Offset.Zero
                    },
                    onDragCancel = {
                        dragging = false
                        dragOffset = Offset.Zero
                    }) { change, dragAmount ->
                    change.consumeAllChanges()

                    dragOffset += dragAmount
                }
            }

    ) {


    }
}