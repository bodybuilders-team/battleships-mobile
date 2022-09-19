package pt.isel.pdm.battleships.ui.ship

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import pt.isel.pdm.battleships.domain.ship.Orientation
import pt.isel.pdm.battleships.domain.ship.Ship
import pt.isel.pdm.battleships.ui.board.TILE_SIZE

/**
 * Visual representation of a ship.
 *
 * @param ship the ship to be represented
 */
@Composable
fun ShipView(ship: Ship) {
    val point = ship.coordinate.toPoint()

    Box(
        Modifier
            .offset(
                x = (point.first * TILE_SIZE).dp,
                y = (point.second * TILE_SIZE).dp
            )
            .size(
                width = (
                    TILE_SIZE *
                        if (ship.orientation == Orientation.HORIZONTAL) ship.type.size else 1
                    ).dp,

                height = (
                    TILE_SIZE *
                        if (ship.orientation == Orientation.VERTICAL) ship.type.size else 1
                    ).dp
            )
            .background(Color.Black)
    )
}
