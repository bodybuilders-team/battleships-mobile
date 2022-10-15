package pt.isel.pdm.battleships.domain.board

import pt.isel.pdm.battleships.domain.Cell
import pt.isel.pdm.battleships.domain.ShipCell
import pt.isel.pdm.battleships.domain.ship.Ship
import pt.isel.pdm.battleships.utils.replace
import java.io.Serializable

data class MyBoard(
    override val size: Int = DEFAULT_BOARD_SIZE,
    override val grid: List<Cell> = generateEmptyMatrix(size)
) : Board(size, grid), Serializable {

    init {
        isValid()
    }

    val fleet: List<Ship>
        get() = grid.filterIsInstance<ShipCell>().map { it.ship }.distinct()

    /**
     * Returns a new board with the cell in [at] coordinate replaced by [cell].
     *
     * @param at coordinate of the cell to be replaced
     * @param cell cell to replace the cell in [at] coordinate
     *
     * @return new board with the cell in [at] coordinate replaced by [cell]
     */
    private fun setCell(at: Coordinate, cell: Cell) =
        copy(grid = grid.replace(at.toIndex(size), cell))
}
