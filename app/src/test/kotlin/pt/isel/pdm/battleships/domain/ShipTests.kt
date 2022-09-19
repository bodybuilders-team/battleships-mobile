package pt.isel.pdm.battleships.domain

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import pt.isel.pdm.battleships.domain.board.Coordinate
import pt.isel.pdm.battleships.domain.ship.Ship
import pt.isel.pdm.battleships.domain.ship.ShipType

class ShipTests {

    @Test
    fun `Instantiate Ship coordinates size is equal to ship type size`() {
        Ship(
            ShipType.DESTROYER,
            listOf(Coordinate('A', 1), Coordinate('A', 2)),
            2
        )
    }

    @Test(expected = IllegalArgumentException::class)
    fun `Instantiate Ship coordinates size is not equal to ship type size throws`() {
        Ship(
            ShipType.DESTROYER,
            listOf(Coordinate('A', 2)),
            2
        )
    }

    @Test
    fun `Ship is sunk when it has no more lives`() {
        val ship = Ship(
            ShipType.DESTROYER,
            listOf(Coordinate('A', 1), Coordinate('A', 2)),
            0
        )

        assertTrue { ship.isSunk }
    }

    @Test
    fun `Ship is not sunk when it has more than 0 lives`() {
        val ship = Ship(
            ShipType.DESTROYER,
            listOf(Coordinate('A', 1), Coordinate('A', 2)),
            2
        )

        assertFalse { ship.isSunk }
    }
}
