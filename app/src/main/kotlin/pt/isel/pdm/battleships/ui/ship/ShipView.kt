package pt.isel.pdm.battleships.ui

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

@Composable
fun ShipView(ship: Ship) {
    val point = ship.position.toPoint()

    Box(
        Modifier
            .offset(
                (point.first * TILE_SIZE).dp,
                (point.second * TILE_SIZE).dp
            )
            .size(
                (
                        TILE_SIZE * if (ship.orientation == Orientation.HORIZONTAL) {
                            ship.type.size
                        } else 1
                        ).dp,

                (
                        TILE_SIZE * if (ship.orientation == Orientation.VERTICAL) {
                            ship.type.size
                        } else 1
                        ).dp
            )
            .background(Color.Black)
    )
}
