package pt.isel.pdm.battleships.domain

/**
 * Represents a board in the game.
 * The boards are typically square – usually 10×10 – and the individual squares in the grid are identified by letter and number.
 *
 * @param matrix the matrix of cells
 */
data class Board(private val matrix: List<Cell>) {

    /**
     * Returns the matrix index obtained from the coordinate.
     *
     * @return the matrix index obtained from the coordinate
     */
    private fun Coordinate.toIndex() =
        (BOARD_SIDE_LENGTH - row) * BOARD_SIDE_LENGTH + (col - Coordinate.COLS_RANGE.first)

    /**
     * Returns the cell in [coordinate].
     *
     * @param coordinate coordinate to get cell of
     * @return cell in [coordinate]
     */
    fun getCell(coordinate: Coordinate) = matrix[coordinate.toIndex()]

    companion object {
        const val BOARD_SIDE_LENGTH = 10
    }
}
