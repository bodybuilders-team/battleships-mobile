package pt.isel.pdm.battleships.domain.board

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
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
@Parcelize
data class Coordinate(val col: Char, val row: Int) : Parcelable {

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

    companion object {
        val maxColsRange = FIRST_COL until FIRST_COL + MAX_BOARD_SIZE
        val maxRowsRange = FIRST_ROW..MAX_BOARD_SIZE

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

    override fun toString() = "$col$row"
}
