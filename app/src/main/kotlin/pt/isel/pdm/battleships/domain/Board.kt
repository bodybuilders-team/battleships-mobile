package pt.isel.pdm.battleships.domain

import pt.isel.pdm.battleships.domain.exceptions.InvalidAttackException
import pt.isel.pdm.battleships.utils.replace

/**
 * Represents a board in the game.
 * The boards are typically square – usually 10×10 – and the individual squares in the grid are identified by letter and number.
 *
 * @property matrix the matrix of cells
 * @property fleet the fleet of ships
 */
class Board(
    private val matrix: List<Cell>
) {
    init {
        val boardSize = BOARD_SIDE_LENGTH * BOARD_SIDE_LENGTH
        require(matrix.size == boardSize) { "Board must have $boardSize cells" }
        require(fleet.size == NUMBER_OF_SHIPS) { "Board must have $NUMBER_OF_SHIPS ships" }

        // TODO: Validations
    }

    val fleet: List<Ship>
        get() = matrix.filterIsInstance<ShipCell>().map { it.ship }.distinct()

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

    /**
     * Places a ship in the board.
     *
     * @param ship the ship to place
     * @return the new board with the ship placed
     */
    fun placeShip(ship: Ship): Board {
        // TODO: to be implemented
    }

    /**
     * Attacks the cell in [coordinate].
     * If the cell is a ship cell, the ship is hit.
     * If the cell is a hit cell, the attack is invalid.
     * If the cell is a miss cell, the attack is invalid.
     * If the cell is a water cell, the cell is changed to a miss cell.
     * If the cell is a ship cell and the cell is not hit, the cell is changed to a hit cell.
     *
     * @param coordinate coordinate to attack
     * @return the board after the attack
     */
    fun attack(coordinate: Coordinate): Board =
        when (val cell = getCell(coordinate)) {
            is ShipCell -> {
                if (cell.wasHit) throw InvalidAttackException("Cell $coordinate was already hit")

                val ship = cell.ship
                val newShip = ship.copy(lives = ship.lives - 1)

                Board(
                    matrix.map { currCell ->
                        when (currCell) {
                            is ShipCell -> currCell.copy(
                                ship = newShip,
                                wasHit = currCell.wasHit || currCell.ship == ship
                            )
                            else -> currCell
                        }
                    }
                )
            }
            is MissCell -> throw InvalidAttackException("Cell $coordinate was already hit")
            is WaterCell -> Board(matrix.replace(coordinate.toIndex(), MissCell(coordinate)))
        }

    companion object {
        const val BOARD_SIDE_LENGTH = 10
        const val NUMBER_OF_SHIPS = 5
    }
}
