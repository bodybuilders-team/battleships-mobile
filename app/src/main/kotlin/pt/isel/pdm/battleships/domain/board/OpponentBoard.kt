package pt.isel.pdm.battleships.domain.board

import pt.isel.pdm.battleships.domain.Cell
import pt.isel.pdm.battleships.domain.ShipCell
import pt.isel.pdm.battleships.domain.WaterCell
import pt.isel.pdm.battleships.domain.exceptions.InvalidShotException
import pt.isel.pdm.battleships.domain.ship.Ship
import pt.isel.pdm.battleships.utils.replace
import pt.isel.pdm.battleships.utils.replaceIf

data class OpponentBoard(
    override val size: Int = DEFAULT_BOARD_SIZE,
    override val grid: List<Cell> = generateEmptyMatrix(size)
) : Board(size, grid) {

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

    /**
     * Shoots the cell in [coordinate].
     * If the cell is already hit, the attack is invalid.
     * Otherwise, the cell becomes hit.
     *
     * @param coordinate coordinate to attack
     *
     * @return the board after the attack
     * @throws InvalidShotException if the attack is invalid
     */
    fun shoot(coordinate: Coordinate): OpponentBoard {
        val cell = getCell(coordinate)

        if (cell.wasHit) throw InvalidShotException("Cell $coordinate was already hit")

        return when (cell) {
            is ShipCell -> {
                val ship = cell.ship
                val newShip = ship.copy(lives = ship.lives - 1)

                copy(
                    grid = grid.replaceIf(
                        predicate = { it is ShipCell && it.ship == ship },
                        new = {
                            (it as ShipCell).copy(
                                ship = newShip,
                                wasHit = it.wasHit || it == cell
                            )
                        }
                    )
                )
            }
            is WaterCell -> setCell(coordinate, cell.copy(wasHit = true))
        }
    }
}
