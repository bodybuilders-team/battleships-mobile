package pt.isel.pdm.battleships.domain.games

import pt.isel.pdm.battleships.service.services.games.models.CoordinateModel
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CoordinateTests {

    @Test(expected = IllegalArgumentException::class)
    fun `Instantiate Coordinate with column outside bounds throws`() {
        Coordinate(col = 'Z', row = 2)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `Instantiate Coordinate with row outside bounds throws`() {
        Coordinate(col = 'A', row = 19)
    }

    @Test
    fun `Instantiate Coordinate with column and row inside bounds`() {
        Coordinate(col = 'A', row = 5)
    }

    @Test
    fun `toCoordinateModel returns a CoordinateModel with the same column and row`() {
        val coordinateModel = CoordinateModel(col = 'A', row = 5)
        assertEquals(coordinateModel, Coordinate(col = 'A', row = 5).toCoordinateModel())
    }

    @Test
    fun `Coordinate toString works`() {
        assertEquals("A5", Coordinate(col = 'A', row = 5).toString())
    }

    @Test
    fun `Coordinate isValid with column and row inside bounds`() {
        assertTrue { Coordinate.isValid(col = 'A', row = 5) }
    }

    @Test
    fun `Coordinate isValid with column outside bounds`() {
        assertTrue { !Coordinate.isValid(col = 'Z', row = 5) }
    }
}
