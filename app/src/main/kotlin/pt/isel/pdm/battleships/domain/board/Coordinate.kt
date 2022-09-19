package pt.isel.pdm.battleships.domain.board

/**
 * Coordinate of each board cell.
 *
 * @property col char in range [COLS_RANGE]
 * @property row int in range [ROWS_RANGE]
 */
data class Coordinate(val col: Char, val row: Int) {
    init {
        require(col in COLS_RANGE) {
            "Invalid Coordinate: " +
                "Column $col out of range (${COLS_RANGE.first} .. ${COLS_RANGE.last})."
        }
        require(row in ROWS_RANGE) {
            "Invalid Coordinate:" +
                " Row $row out of range (${ROWS_RANGE.first} .. ${ROWS_RANGE.last})."
        }
    }

    override fun toString() = "$col$row"

    companion object {
        val COLS_RANGE = 'A'..'J'
        val ROWS_RANGE = 1..Board.BOARD_SIDE_LENGTH
    }
}
