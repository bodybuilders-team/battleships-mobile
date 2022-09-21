/*
package pt.isel.pdm.battleships.domain.board

import pt.isel.pdm.battleships.domain.Cell
import pt.isel.pdm.battleships.domain.MissCell
import pt.isel.pdm.battleships.domain.ShipCell
import pt.isel.pdm.battleships.domain.WaterCell
import pt.isel.pdm.battleships.domain.exceptions.InvalidAttackException
import pt.isel.pdm.battleships.domain.ship.Orientation
import pt.isel.pdm.battleships.domain.ship.Ship
import pt.isel.pdm.battleships.domain.ship.ShipType
import pt.isel.pdm.battleships.utils.replace

*/
/**
 * Represents a board in the game.
 * The boards are typically square – usually 10×10 – and the individual squares in the grid are identified by letter and number.
 *
 * @property grid the matrix of cells
 * @property fleet the fleet of ships
 *//*

class Board(
    private val grid: List<Cell>,
    val size: Int = grid.size
) {
    init {
        require(grid.size == size) { "Board must have $size cells" }
        require(size in MIN_BOARD_SIDE_LENGTH..MAX_BOARD_SIDE_LENGTH) {
            "Board size must be between 7 and 20"
        }
        // TODO: Validations
    }

    */
/**
     * Generates a board with the given type.
     *
     * @param size the size of the board
     * @param type the type of the board
     *//*

    constructor(type: BoardType = BoardType.EMPTY, size: Int = DEFAULT_BOARD_SIDE_LENGTH) : this(
        when (type) {
            BoardType.EMPTY -> generateEmptyMatrix(size)
            BoardType.RANDOM -> generateRandomMatrix(size)
        }
    )

    */
/**
     * Represents the board types.
     *//*

    enum class BoardType {
        EMPTY, RANDOM
    }

    val fleet: List<Ship>
        get() = grid.filterIsInstance<ShipCell>().map { it.ship }.distinct()

    */
/**
     * Returns the cell in [coordinate].
     *
     * @param coordinate coordinate to get cell of
     * @return cell in [coordinate]
     *//*

    fun getCell(coordinate: Coordinate) = grid[coordinate.toIndex(size)]

    */
/**
     * Checks if it is possible to place a ship in its coordinates.
     *
     * @param ship ship to check
     * @return true if it is possible to place the ship in its coordinates, false otherwise
     *//*

    fun canPlaceShip(ship: Ship) = ship.coordinates.all { getCell(it) is WaterCell }

    */
/**
     * Places a ship in the board.
     *
     * @param ship the ship to place
     * @return the new board with the ship placed
     *//*

    fun placeShip(ship: Ship) = Board(
        grid.map {
            if (it.coordinate in ship.coordinates) {
                ShipCell(it.coordinate, ship)
            } else {
                it
            }
        }
    )

    */
/**
     * Shoots the cell in [coordinate].
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
     *//*

    fun shoot(coordinate: Coordinate): Board =
        when (val cell = getCell(coordinate)) {
            is ShipCell -> {
                if (cell.wasHit) throw InvalidAttackException("Cell $coordinate was already hit")

                val ship = cell.ship
                val newShip = ship.copy(lives = ship.lives - 1)

                Board(
                    grid.map { currCell ->
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
            is WaterCell -> Board(grid.replace(coordinate.toIndex(size), MissCell(coordinate)))
        }

    companion object {
        const val DEFAULT_BOARD_SIDE_LENGTH = 10
        const val MIN_BOARD_SIDE_LENGTH = 7
        const val MAX_BOARD_SIDE_LENGTH = 20



        */
/**
         * Generates a matrix only with water cells.
         *
         * @param size the size of the matrix
         * @return generated matrix
         *//*

        private fun generateEmptyMatrix(size: Int): List<Cell> =
            List(size * size) {
                WaterCell(
                    Coordinate(
                        row = size - it / size,
                        col = getColumnsRange(size).first + it % size
                    )
                )
            }

        */
/**
         * Returns the matrix index obtained from the coordinate.
         *
         * @param size the size of the matrix
         * @return the matrix index obtained from the coordinate
         *//*

        fun Coordinate.toIndex(size: Int) =
            (size - row) * size + (col - getColumnsRange(size).first)

        */
/**
         * Generates a matrix of cells with the ships placed randomly.
         *
         * @param size the size of the matrix
         * @return the matrix
         *//*

        private fun generateRandomMatrix(size: Int): List<Cell> {
            val matrix = generateEmptyMatrix(size).toMutableList()

            ShipType.values().forEach { shipType ->
                val possibleCombinations: List<Pair<List<Coordinate>, Orientation>> = matrix
                    .filterIsInstance<WaterCell>()
                    .flatMap { cell ->
                        Orientation.values()
                            .fold(emptyList()) { acc, orientation ->
                                if (Ship.isValidShipCoordinate(
                                        cell.coordinate.col - getColumnsRange(size).first,
                                        cell.coordinate.row,
                                        orientation,
                                        shipType.size,
                                        size
                                    )
                                ) {
                                    acc + Pair(
                                        Ship.getCoordinates(shipType, cell.coordinate, orientation, size),
                                        orientation
                                    )
                                } else acc
                            }
                    }

                val coordinates = possibleCombinations
                    .filter { (coord, _) -> coord.all { matrix[it.toIndex(size)] is WaterCell } }
                    .random()

                val ship = Ship(
                    shipType,
                    coordinate = coordinates.first.first(),
                    orientation = coordinates.second
                )

                coordinates.first.forEach {
                    matrix[it.toIndex(size)] = ShipCell(it, ship)
                }
            }

            return matrix
        }
    }
}
*/
