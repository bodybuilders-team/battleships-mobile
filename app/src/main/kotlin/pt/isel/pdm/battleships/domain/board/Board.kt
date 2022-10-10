package pt.isel.pdm.battleships.domain.board

import pt.isel.pdm.battleships.domain.Cell
import pt.isel.pdm.battleships.domain.ShipCell
import pt.isel.pdm.battleships.domain.WaterCell
import pt.isel.pdm.battleships.domain.exceptions.InvalidShotException
import pt.isel.pdm.battleships.domain.ship.Orientation
import pt.isel.pdm.battleships.domain.ship.Ship
import pt.isel.pdm.battleships.domain.ship.ShipType
import pt.isel.pdm.battleships.utils.replace
import pt.isel.pdm.battleships.utils.replaceIf

/**
 * Represents a board in the game.
 *
 * @property size the size of the board
 * @property grid the matrix of cells
 *
 * @property fleet the fleet of ships
 */
data class Board(
    val size: Int = DEFAULT_BOARD_SIZE,
    private val grid: List<Cell> = generateEmptyMatrix(size)
) {

    val fleet: List<Ship>
        get() = grid.filterIsInstance<ShipCell>().map { it.ship }.distinct()

    init {
        require(size in MIN_BOARD_SIZE..MAX_BOARD_SIZE) {
            "Board size must be between $MIN_BOARD_SIZE and $MAX_BOARD_SIZE"
        }

        require(grid.size == size * size) {
            "Grid size must be equal to $size * $size"
        }
    }

    /**
     * Returns the cell in [coordinate].
     *
     * @param coordinate coordinate to get cell of
     * @return cell in [coordinate]
     */
    fun getCell(coordinate: Coordinate) = grid[coordinate.toIndex(size)]

    /**
     * Returns a new board with the cell in [at] coordinate replaced by [cell].
     *
     * @param at coordinate of the cell to be replaced
     * @param cell cell to replace the cell in [at] coordinate
     *
     * @return new board with the cell in [at] coordinate replaced by [cell]
     */
    fun setCell(at: Coordinate, cell: Cell) = copy(grid = grid.replace(at.toIndex(size), cell))

    /**
     * Checks if it is possible to place a ship in its coordinates.
     *
     * @param ship ship to check
     * @return true if it is possible to place the ship in its coordinates, false otherwise
     */
    fun canPlaceShip(ship: Ship) = ship.coordinates.all {
        getCell(it).let { cell -> !cell.wasHit && cell is WaterCell }
    }

    /**
     * Places a ship in the board.
     *
     * @param ship the ship to place
     * @return the new board with the ship placed
     */
    fun placeShip(ship: Ship): Board {
        require(canPlaceShip(ship)) { "Cannot place ship in its coordinates" }

        return copy(
            grid = grid.replaceIf(
                predicate = { ship.coordinates.contains(it.coordinate) },
                new = { ShipCell(it.coordinate, it.wasHit, ship) }
            )
        )
    }

    /**
     * Removes a ship from the board.
     *
     * @param ship the ship to place
     * @return the new board with the ship placed
     */
    fun removeShip(ship: Ship): Board {
        return copy(
            grid = grid.replaceIf(
                predicate = { ship.coordinates.contains(it.coordinate) },
                new = { WaterCell(it.coordinate, it.wasHit) }
            )
        )
    }

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
    fun shoot(coordinate: Coordinate): Board {
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

    companion object {
        const val DEFAULT_BOARD_SIZE = 10
        const val MIN_BOARD_SIZE = 7
        const val MAX_BOARD_SIZE = 26
        const val FIRST_COL = 'A'
        const val FIRST_ROW = 1

        /**
         * Gets the column range values for a board of [size].
         *
         * @param size size of the board
         * @return column range values
         */
        fun getColumnsRange(size: Int) = FIRST_COL until FIRST_COL + size

        /**
         * Gets the row range values for a board of [size].
         *
         * @param size size of the board
         * @return row range values
         */
        fun getRowsRange(size: Int) = FIRST_ROW..size

        /**
         * Returns the matrix index obtained from the coordinate.
         *
         * @param size the size of the matrix
         *
         * @return the matrix index obtained from the coordinate
         * @throws IllegalArgumentException if the coordinate is out of bounds
         */
        fun Coordinate.toIndex(size: Int): Int {
            val colsRange = getColumnsRange(size)
            val rowsRange = getRowsRange(size)

            require(col in colsRange) {
                "Invalid Coordinate: Column $col out of range $colsRange."
            }
            require(row in rowsRange) {
                "Invalid Coordinate: Row $row out of range $rowsRange."
            }

            return (size - row) * size + (col - FIRST_COL)
        }

        /**
         * Returns a random board of [size].
         *
         * @param size size of the board
         * @param ships the ships to place in the board
         *
         * @return board of [size] with the [ships] placed randomly
         */
        fun random(
            size: Int = DEFAULT_BOARD_SIZE,
            ships: List<ShipType> = ShipType.values().toList()
        ) =
            Board(size, generateRandomMatrix(size, ships))

        /**
         * Generates a matrix only with water cells.
         *
         * @param size the size of the matrix
         * @return generated matrix
         */
        fun generateEmptyMatrix(size: Int): List<Cell> =
            List(size * size) {
                WaterCell(
                    Coordinate(
                        row = size - it / size,
                        col = getColumnsRange(size).first + it % size
                    ),
                    wasHit = false
                )
            }

        /**
         * Generates a matrix of cells with the ships placed randomly.
         *
         * @param size the size of the matrix
         * @param ships the ships to place in the matrix
         *
         * @return matrix of [size] with the [ships] placed randomly
         */
        private fun generateRandomMatrix(size: Int, ships: List<ShipType>): List<Cell> {
            val grid = generateEmptyMatrix(size).toMutableList()

            ships.forEach { shipType ->
                val ship = grid
                    .filterIsInstance<WaterCell>()
                    .flatMap<WaterCell, Ship> { cell ->
                        Orientation.values()
                            .fold(emptyList()) { acc, orientation ->
                                val coordinates = Ship.getCoordinates(
                                    shipType,
                                    cell.coordinate,
                                    orientation
                                )
                                if (Ship.isValidShipCoordinate(
                                        cell.coordinate,
                                        orientation,
                                        shipType.size,
                                        size
                                    ) &&
                                    coordinates.all { coordinate ->
                                        grid[coordinate.toIndex(size)] is WaterCell
                                    }
                                ) {
                                    acc + Ship(shipType, coordinates.first(), orientation)
                                } else acc
                            }
                    }
                    .random()

                ship.coordinates.forEach {
                    grid[it.toIndex(size)] = ShipCell(coordinate = it, wasHit = false, ship = ship)
                }
            }

            return grid
        }
    }
}
