package pt.isel.pdm.battleships.ui.screens.gameplay.ship

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import pt.isel.pdm.battleships.domain.ship.Orientation
import pt.isel.pdm.battleships.domain.ship.Ship
import pt.isel.pdm.battleships.ui.screens.gameplay.board.TILE_SIZE

private const val SHIP_CORNER_RADIUS = 16

/**
 * Visual representation of a ship.
 *
 * @param ship the ship to be represented
 * @param offset the offset of the ship
 */
@Composable
fun ShipView(ship: Ship, offset: Offset = Offset.Zero) {
    val point = ship.coordinate.toPoint()

    Box(
        Modifier
            .offset(
                x = (offset.x + point.first * TILE_SIZE).dp,
                y = (offset.y + point.second * TILE_SIZE).dp
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
            .clip(RoundedCornerShape(SHIP_CORNER_RADIUS.dp))
            .background(Color.DarkGray)
    )
}
