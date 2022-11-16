package pt.isel.pdm.battleships.domain.games

import pt.isel.pdm.battleships.domain.games.Coordinate.Companion.maxColsRange
import pt.isel.pdm.battleships.domain.games.Coordinate.Companion.maxRowsRange
import pt.isel.pdm.battleships.domain.games.board.Board
import pt.isel.pdm.battleships.services.games.models.CoordinateModel

/**
 * Coordinate of each board cell.
 *
 * @property col char in range [maxColsRange]
 * @property row int in range [maxRowsRange]
 */
data class Coordinate(val col: Char, val row: Int) {

    init {
        require(col in maxColsRange) {
            "Invalid Coordinate: " +
                "Column $col out of range (${maxColsRange.first} .. ${maxColsRange.last})."
        }
        require(row in maxRowsRange) {
            "Invalid Coordinate:" +
                " Row $row out of range (${maxRowsRange.first} .. ${maxRowsRange.last})."
        }
    }

    /**
     * Converts a Coordinate to a CoordinateModel.
     *
     * @return the CoordinateModel
     */
    fun toCoordinateModel(): CoordinateModel = CoordinateModel(col = col, row = row)

    override fun toString() = "$col$row"

    companion object {
        val maxColsRange = Board.FIRST_COL until Board.FIRST_COL + Board.MAX_BOARD_SIZE
        val maxRowsRange = Board.FIRST_ROW..Board.MAX_BOARD_SIZE

        val first = Coordinate(maxColsRange.first, maxRowsRange.first)

        /**
         * Checks if a coordinate is valid.
         *
         * @param col char that must be in range [maxColsRange]
         * @param row int that must be in range [maxRowsRange]
         *
         * @return true if the coordinate is valid, false otherwise
         */
        fun isValid(col: Char, row: Int) = col in maxColsRange && row in maxRowsRange
    }
}
