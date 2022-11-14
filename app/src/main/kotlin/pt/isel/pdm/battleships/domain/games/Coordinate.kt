package pt.isel.pdm.battleships.domain.games

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
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

    /**
     * Converts a Coordinate to a CoordinateDTO.
     *
     * @return the CoordinateDTO
     */
    fun toCoordinateDTO(): CoordinateModel = CoordinateModel(col = col, row = row)

    override fun toString() = "$col$row"

    companion object {
        val maxColsRange = Board.FIRST_COL until Board.FIRST_COL + Board.MAX_BOARD_SIZE
        val maxRowsRange = Board.FIRST_ROW..Board.MAX_BOARD_SIZE

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
