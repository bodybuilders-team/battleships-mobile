package pt.isel.pdm.battleships.domain.board

import pt.isel.pdm.battleships.domain.board.Board.Companion.FIRST_COL
import pt.isel.pdm.battleships.domain.board.Board.Companion.FIRST_ROW
import pt.isel.pdm.battleships.domain.board.Board.Companion.MAX_BOARD_SIZE
import pt.isel.pdm.battleships.domain.board.Coordinate.Companion.maxColsRange
import pt.isel.pdm.battleships.domain.board.Coordinate.Companion.maxRowsRange

/**
 * Coordinate of each board cell.
 *
 * @property col char in range [maxColsRange]
 * @property row int in range [maxRowsRange]
 */
data class Coordinate(val col: Char, val row: Int) { // TODO Rename to column?

    init {
        require(col in maxColsRange) {
            "Invalid Coordinate: " +
                "Column $col out of range (${maxColsRange.first} .. ${maxColsRange.last})."
        }
        require(row in maxRowsRange) {
            "Invalid Coordinate:" +
                " Row $row out of range (${maxColsRange.first} .. ${maxColsRange.last})."
        }
    }

    companion object {
        val maxColsRange = FIRST_COL until FIRST_COL + MAX_BOARD_SIZE
        val maxRowsRange = FIRST_ROW..MAX_BOARD_SIZE

        fun isValid(col: Char, row: Int) = col in maxColsRange && row in maxRowsRange
    }

    override fun toString() = "$col$row"
}
