package pt.isel.pdm.battleships.domain.games.board

import pt.isel.pdm.battleships.domain.games.Cell
import pt.isel.pdm.battleships.domain.games.Coordinate
import pt.isel.pdm.battleships.domain.games.ShipCell
import pt.isel.pdm.battleships.domain.games.ship.Ship
import pt.isel.pdm.battleships.domain.utils.replace

/**
 * Represents a board of the player.
 *
 * @param size the size of the board
 * @property grid the grid of the board
 *
 * @property fleet the fleet of the board
 */
data class MyBoard(
    override val size: Int = DEFAULT_BOARD_SIZE,
    override val grid: List<Cell> = generateEmptyMatrix(size)
) : Board(size, grid) {

    init {
        isValid()
    }

    companion object {
        operator fun invoke(size: Int, initialFleet: List<Ship>): MyBoard {
            val grid = generateEmptyMatrix(size).toMutableList()

            initialFleet.forEach { ship ->
                ship.coordinates.forEach { coordinate ->
                    grid[coordinate.toIndex(size)] =
                        ShipCell(
                            coordinate = coordinate,
                            wasHit = false,
                            ship = ship
                        )
                }
            }

            return MyBoard(size, grid)
        }
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
