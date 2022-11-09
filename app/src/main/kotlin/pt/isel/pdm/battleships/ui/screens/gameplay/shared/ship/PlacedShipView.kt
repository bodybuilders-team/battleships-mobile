package pt.isel.pdm.battleships.ui.screens.gameplay.shared.ship

import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pt.isel.pdm.battleships.domain.games.Coordinate
import pt.isel.pdm.battleships.domain.games.board.Board
import pt.isel.pdm.battleships.domain.games.ship.Ship

/**
 * Visual representation of a ship placed on the board.
 *
 * @param ship the ship to be represented
 * @param tileSize the size of the tile
 */
@Composable
fun PlacedShipView(
    ship: Ship,
    tileSize: Float
) {
    val (xPoint, yPoint) = ship.coordinate.toPoint()

    ShipView(
        type = ship.type,
        orientation = ship.orientation,
        tileSize = tileSize,
        modifier = Modifier
            .offset(
                x = (xPoint * tileSize).dp,
                y = (yPoint * tileSize).dp
            )
    )
}

/**
 * Converts a [Coordinate] to a point.
 *
 * @return a pair of integers representing the point
 */
fun Coordinate.toPoint(): Pair<Int, Int> = Pair(col - Board.FIRST_COL, row - 1)
