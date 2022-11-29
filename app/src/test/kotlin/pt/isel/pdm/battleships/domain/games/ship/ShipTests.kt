package pt.isel.pdm.battleships.domain.games.ship

import pt.isel.pdm.battleships.domain.games.Coordinate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ShipTests {

    @Test
    fun `Ship coordinates are correct when vertical`() {
        val ship = Ship(
            ShipType.DESTROYER,
            Coordinate('A', 1),
            Orientation.VERTICAL
        )

        val expectedCoordinates = listOf(
            Coordinate('A', 1),
            Coordinate('A', 2)
        )

        assertEquals(expectedCoordinates, ship.coordinates)
    }

    @Test
    fun `Ship coordinates are correct when horizontal`() {
        val ship = Ship(
            ShipType.DESTROYER,
            Coordinate('A', 1),
            Orientation.HORIZONTAL
        )

        val expectedCoordinates = listOf(
            Coordinate('A', 1),
            Coordinate('B', 1)
        )

        assertEquals(expectedCoordinates, ship.coordinates)
    }

    @Test
    fun `Ship getCoordinates calculates coordinates correctly when vertical`() {
        val shipType = ShipType.DESTROYER
        val coordinate = Coordinate('A', 1)
        val orientation = Orientation.VERTICAL

        val expectedCoordinates = listOf(
            Coordinate('A', 1),
            Coordinate('A', 2)
        )

        assertEquals(expectedCoordinates, Ship.getCoordinates(shipType, coordinate, orientation))
    }

    @Test
    fun `Ship getCoordinates calculates coordinates correctly when horizontal`() {
        val shipType = ShipType.DESTROYER
        val coordinate = Coordinate('A', 1)
        val orientation = Orientation.HORIZONTAL

        val expectedCoordinates = listOf(
            Coordinate('A', 1),
            Coordinate('B', 1)
        )

        assertEquals(expectedCoordinates, Ship.getCoordinates(shipType, coordinate, orientation))
    }

    @Test
    fun `Ship isValidShipCoordinate returns true when coordinate is inside the board`() {
        assertTrue {
            Ship.isValidShipCoordinate(
                coordinate = Coordinate('A', 1),
                orientation = Orientation.VERTICAL,
                size = 2,
                boardSize = 10
            )
        }
    }

    @Test
    fun `Ship isValidShipCoordinate returns false when coordinate is outside the board`() {
        assertFalse {
            Ship.isValidShipCoordinate(
                coordinate = Coordinate('B', 2),
                orientation = Orientation.VERTICAL,
                size = 2,
                boardSize = 1
            )
        }
    }
}
