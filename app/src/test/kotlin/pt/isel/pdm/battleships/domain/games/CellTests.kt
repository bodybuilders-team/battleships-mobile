package pt.isel.pdm.battleships.domain.games

import pt.isel.pdm.battleships.domain.games.ship.Orientation
import pt.isel.pdm.battleships.domain.games.ship.Ship
import pt.isel.pdm.battleships.domain.games.ship.ShipType
import kotlin.test.Test

class CellTests {

    @Test
    fun `Instantiate WaterCell`() {
        WaterCell(
            coordinate = Coordinate(col = 'A', row = 1),
            wasHit = false
        )
    }

    @Test
    fun `Instantiate ShipCell`() {
        val coordinate = Coordinate(col = 'A', row = 1)
        ShipCell(
            coordinate = coordinate,
            wasHit = false,
            ship = Ship(
                type = ShipType.BATTLESHIP,
                orientation = Orientation.HORIZONTAL,
                coordinate = coordinate
            )
        )
    }
}
