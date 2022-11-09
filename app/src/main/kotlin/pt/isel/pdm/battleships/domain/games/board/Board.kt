package pt.isel.pdm.battleships.domain.games.board

import pt.isel.pdm.battleships.domain.games.Cell
import pt.isel.pdm.battleships.domain.games.Coordinate
import pt.isel.pdm.battleships.domain.games.WaterCell

/**
 * Represents a board in the game.
 *
 * @property size the size of the board
 * @property grid the grid of cells
 */
abstract class Board(
    open val size: Int = DEFAULT_BOARD_SIZE,
    protected open val grid: List<Cell> = generateEmptyMatrix(size)
) {
    /**
     * Checks if the board is valid.
     *
     * @throws IllegalArgumentException if the board is not valid
     */
    protected fun isValid() {
        require(size in MIN_BOARD_SIZE..MAX_BOARD_SIZE) {
            "Board size must be between $size $MIN_BOARD_SIZE and $MAX_BOARD_SIZE"
        }

        require(grid.size == size * size) {
            "Grid size must be equal to $size * $size"
        }
    }

    /**
     * Returns the cell in [coordinate].
     *
     * @param coordinate coordinate to get cell of
     * @return cell in [coordinate]
     */
    fun getCell(coordinate: Coordinate) = grid[coordinate.toIndex(size)]

    companion object {
        const val DEFAULT_BOARD_SIZE = 10
        const val MIN_BOARD_SIZE = 7
        const val MAX_BOARD_SIZE = 18
        const val FIRST_COL = 'A'
        const val FIRST_ROW = 1

        /**
         * Gets the column range values for a board of [size].
         *
         * @param size size of the board
         * @return column range values
         */
        fun getColumnsRange(size: Int) = FIRST_COL until FIRST_COL + size

        /**
         * Gets the row range values for a board of [size].
         *
         * @param size size of the board
         * @return row range values
         */
        fun getRowsRange(size: Int) = FIRST_ROW..size

        /**
         * Returns the matrix index obtained from the coordinate.
         *
         * @param size the size of the matrix
         *
         * @return the matrix index obtained from the coordinate
         * @throws IllegalArgumentException if the coordinate is out of bounds
         */
        fun Coordinate.toIndex(size: Int): Int {
            val colsRange = getColumnsRange(size)
            val rowsRange = getRowsRange(size)

            require(col in colsRange) {
                "Invalid Coordinate: Column $col out of range $colsRange."
            }
            require(row in rowsRange) {
                "Invalid Coordinate: Row $row out of range $rowsRange."
            }

            return (size - row) * size + (col - FIRST_COL)
        }

        /**
         * Generates a matrix only with water cells.
         *
         * @param size the size of the matrix
         * @return generated matrix
         */
        fun generateEmptyMatrix(size: Int): List<Cell> =
            List(size * size) {
                WaterCell(
                    Coordinate(
                        row = size - it / size,
                        col = getColumnsRange(size).first + it % size
                    ),
                    wasHit = false
                )
            }
    }
}
