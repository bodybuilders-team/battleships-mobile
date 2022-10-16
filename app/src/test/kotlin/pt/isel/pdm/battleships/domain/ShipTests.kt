package pt.isel.pdm.battleships.domain

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import pt.isel.pdm.battleships.domain.board.Coordinate
import pt.isel.pdm.battleships.domain.ship.Orientation
import pt.isel.pdm.battleships.domain.ship.Ship
import pt.isel.pdm.battleships.domain.ship.ShipType

class ShipTests {

    @Test
    fun `Ship is sunk when it has no more lives`() {
        val ship = Ship(
            ShipType.DESTROYER,
            Coordinate('A', 1),
            Orientation.VERTICAL,
            0
        )

        assertTrue { ship.isSunk }
    }

    @Test
    fun `Ship is not sunk when it has more than 0 lives`() {
        val ship = Ship(
            ShipType.DESTROYER,
            Coordinate('A', 1),
            Orientation.VERTICAL,
            2
        )

        assertFalse { ship.isSunk }
    }
}
