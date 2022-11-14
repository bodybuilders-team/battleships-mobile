package pt.isel.pdm.battleships.domain.games.board

import pt.isel.pdm.battleships.domain.games.Coordinate
import pt.isel.pdm.battleships.domain.games.WaterCell
import pt.isel.pdm.battleships.domain.games.board.Board.Companion.toCoordinate
import pt.isel.pdm.battleships.domain.games.board.Board.Companion.toIndex
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class BoardTests {

    @Test
    fun `Board default constructor`() {
        val board = object : Board() {
        }
        assertEquals(Board.DEFAULT_BOARD_SIZE, board.size)

        repeat(board.size) {
            repeat(board.size) {
                assertTrue(board.getCell(it.toCoordinate(it + 1)) is WaterCell)
                assertFalse(board.getCell(it.toCoordinate(it + 1)).wasHit)
            }
        }
    }

    @Test
    fun `Board getCell returns the correct cell`() {
        val board = object : Board() {
        }
        val coordinate = Coordinate('A', 1)
        assertEquals(
            WaterCell(coordinate, wasHit = false),
            board.getCell(coordinate)
        )
    }

    @Test
    fun `Board getCell throws when coordinate is out of bounds`() {
        val board = object : Board() {
        }
        val coordinate = Coordinate('A', 11)
        assertFailsWith<IllegalArgumentException> {
            board.getCell(coordinate)
        }
    }

    @Test
    fun `getColumnsRange returns the correct range`() {
        val range = Board.getColumnsRange(10)
        assertEquals('A'..'J', range)
    }

    @Test
    fun `getRowsRange returns the correct range`() {
        val range = Board.getRowsRange(10)
        assertEquals(1..10, range)
    }

    @Test
    fun `Coordinate toIndex returns the correct index`() {
        val index = Coordinate('A', 1).toIndex(10)
        assertEquals(0, index)
    }

    @Test
    fun `Coordinate toIndex throws IllegalArgumentException if the column is out of bounds`() {
        assertFailsWith<IllegalArgumentException> {
            Coordinate('K', 1).toIndex(10)
        }
    }

    @Test
    fun `Coordinate toIndex throws IllegalArgumentException if the row is out of bounds`() {
        assertFailsWith<IllegalArgumentException> {
            Coordinate('A', 11).toIndex(10)
        }
    }

    @Test
    fun `Int toCoordinate returns the correct coordinate`() {
        val coordinate = 0.toCoordinate(10)
        assertEquals(Coordinate('A', 1), coordinate)
    }

    @Test
    fun `Int toCoordinate throws IllegalArgumentException if the index is out of bounds`() {
        assertFailsWith<IllegalArgumentException> {
            100.toCoordinate(10)
        }
    }

    @Test
    fun `generateEmptyMatrix returns the correct matrix`() {
        val matrixSize = 10
        val matrix = Board.generateEmptyMatrix(matrixSize)
        assertEquals(matrixSize * matrixSize, matrix.size)
        matrix.forEachIndexed { index, cell ->
            assertTrue(cell is WaterCell)
            assertFalse(cell.wasHit)

            val expectedCoordinate = Coordinate(
                col = 'A' + index % matrixSize,
                row = index / matrixSize + 1
            )
            assertEquals(expectedCoordinate, cell.coordinate)
        }
    }
}
