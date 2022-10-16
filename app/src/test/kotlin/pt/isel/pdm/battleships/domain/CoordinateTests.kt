package pt.isel.pdm.battleships.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import pt.isel.pdm.battleships.domain.board.Coordinate

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
    fun `Coordinate toString works`() {
        assertEquals("A5", Coordinate(col = 'A', row = 5).toString())
    }
}
