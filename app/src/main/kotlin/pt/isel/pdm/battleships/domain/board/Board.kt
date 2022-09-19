package pt.isel.pdm.battleships.domain.board

import pt.isel.pdm.battleships.domain.Cell
import pt.isel.pdm.battleships.domain.MissCell
import pt.isel.pdm.battleships.domain.ShipCell
import pt.isel.pdm.battleships.domain.WaterCell
import pt.isel.pdm.battleships.domain.exceptions.InvalidAttackException
import pt.isel.pdm.battleships.domain.ship.Ship
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
//        require(fleet.size == NUMBER_OF_SHIPS) { "Board must have $NUMBER_OF_SHIPS ships" }

        // TODO: Validations
    }

    /**
     * Generates a board with the given type.
     *
     * @param type the type of the board
     */
    constructor(type: BoardType = BoardType.EMPTY) : this(
        when (type) {
            BoardType.EMPTY -> generateEmptyMatrix()
            BoardType.RANDOM -> generateRandomMatrix()
        }
    )

    /**
     * Represents the board types.
     */
    enum class BoardType {
        EMPTY, RANDOM
    }

    val fleet: List<Ship>
        get() = matrix.filterIsInstance<ShipCell>().map { it.ship }.distinct()

    /**
     * Returns the matrix index obtained from the coordinate.
     *
     * @return the matrix index obtained from the coordinate
     */
    fun Coordinate.toIndex() =
        (BOARD_SIDE_LENGTH - row) * BOARD_SIDE_LENGTH + (col - Coordinate.COLS_RANGE.first)

    /**
     * Returns the cell in [coordinate].
     *
     * @param coordinate coordinate to get cell of
     * @return cell in [coordinate]
     */
    fun getCell(coordinate: Coordinate) = matrix[coordinate.toIndex()]

    /**
     * Checks if it is possible to place a ship in its coordinates.
     *
     * @param ship ship to check
     * @return true if it is possible to place the ship in its coordinates, false otherwise
     */
    fun canPlaceShip(ship: Ship) = ship.coordinates.all { getCell(it) is WaterCell }

    /**
     * Places a ship in the board.
     *
     * @param ship the ship to place
     * @return the new board with the ship placed
     */
    fun placeShip(ship: Ship) = Board(
        matrix.map {
            if (it.coordinate in ship.coordinates) {
                ShipCell(it.coordinate, ship)
            } else {
                it
            }
        }
    )

    /**
     * Attacks the cell in [coordinate].
     * If the cell is a ship cell, the ship is hit.
     * If the cell is a hit cell, the attack is invalid.
     * If the cell is a miss cell, the attack is invalid.
     * If the cell is a water cell, the cell is changed to a miss cell.
     * If the cell is a ship cell and the cell is not hit, the cell is changed to a hit cell.
     *
     * @param coordinate coordinate to attack
     *
     * @return the board after the attack
     * @throws InvalidAttackException if the attack is invalid
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

        /**
         * Generates a matrix only with water cells.
         *
         * @return generated matrix
         */
        private fun generateEmptyMatrix() =
            List(BOARD_SIDE_LENGTH * BOARD_SIDE_LENGTH) {
                WaterCell(
                    Coordinate(
                        row = BOARD_SIDE_LENGTH - it / BOARD_SIDE_LENGTH,
                        col = Coordinate.COLS_RANGE.first + it % BOARD_SIDE_LENGTH
                    )
                )
            }

        /**
         * Generates a matrix of cells with the ships placed randomly.
         *
         * @return the matrix
         */
        private fun generateRandomMatrix(): List<Cell> {
            val matrix = MutableList(BOARD_SIDE_LENGTH * BOARD_SIDE_LENGTH) { index ->
                val row = BOARD_SIDE_LENGTH - index / BOARD_SIDE_LENGTH
                val col = Coordinate.COLS_RANGE.first + index % BOARD_SIDE_LENGTH

                WaterCell(Coordinate(col, row))
            }

            /*ShipType.values().forEach { shipType ->
                val coordinate = Coordinate(
                    Coordinate.COLS_RANGE.random(),
                    Coordinate.ROWS_RANGE.random()
                )

                val vertical = Random.nextBoolean()

                val coordinates = mutableListOf(coordinate)
                for (i in 1 until shipType.size) {
                    try {
                        coordinates.add(
                            if (vertical) {
                                Coordinate(coordinate.col, coordinate.row + i)
                            } else {
                                Coordinate(coordinate.col + i, coordinate.row)
                            }
                        )
                    } catch (e: IllegalArgumentException) {
                        return@forEach
                    }
                }
                val ship = Ship(shipType, coordinates)
                coordinates.forEach { coordinate ->
                    matrix[coordinate.toIndex()] = ShipCell(coordinate, ship)
                }
            }*/

            return matrix
        }
    }
}
