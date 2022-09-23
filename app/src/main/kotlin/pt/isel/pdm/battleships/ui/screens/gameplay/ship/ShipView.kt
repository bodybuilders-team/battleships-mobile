package pt.isel.pdm.battleships.ui.screens.gameplay.ship

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import pt.isel.pdm.battleships.domain.ship.Orientation
import pt.isel.pdm.battleships.domain.ship.Ship

/**
 * Visual representation of a ship.
 *
 * @param ship the ship to be represented
 * @param offset the offset of the ship
 */
@Composable
fun ShipView(ship: Ship, tileSize: Float, offset: Offset = Offset.Zero) {
    val point = ship.coordinate.toPoint()

    Box(
        Modifier
            .offset(
                x = (offset.x + point.first * tileSize).dp,
                y = (offset.y + point.second * tileSize).dp
            )
            .size(
                width = (
                    tileSize *
                        if (ship.orientation == Orientation.HORIZONTAL) ship.type.size else 1
                    ).dp,

                height = (
                    tileSize *
                        if (ship.orientation == Orientation.VERTICAL) ship.type.size else 1
                    ).dp
            )
    ) {
        ShipImage(
            type = ship.type,
            orientation = ship.orientation
        )
    }
}
