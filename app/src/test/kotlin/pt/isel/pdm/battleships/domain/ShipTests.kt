package pt.isel.pdm.battleships.domain

import pt.isel.pdm.battleships.domain.games.Coordinate
import pt.isel.pdm.battleships.domain.games.ship.Orientation
import pt.isel.pdm.battleships.domain.games.ship.Ship
import pt.isel.pdm.battleships.domain.games.ship.ShipType
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

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
