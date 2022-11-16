package pt.isel.pdm.battleships.domain.games.board

import pt.isel.pdm.battleships.domain.games.Cell
import pt.isel.pdm.battleships.domain.games.ShipCell
import pt.isel.pdm.battleships.domain.games.WaterCell
import pt.isel.pdm.battleships.domain.games.ship.Orientation
import pt.isel.pdm.battleships.domain.games.ship.Ship
import pt.isel.pdm.battleships.domain.games.ship.ShipType
import pt.isel.pdm.battleships.domain.utils.replaceIf

/**
 * A board in the game.
 *
 * @property size the size of the board
 * @property grid the matrix of cells
 *
 * @property fleet the fleet of ships
 */
data class ConfigurableBoard(
    override val size: Int = DEFAULT_BOARD_SIZE,
    override val grid: List<Cell> = generateEmptyMatrix(size)
) : Board(size, grid) {

    init {
        isValid()
    }

    val fleet: List<Ship>
        get() = grid.filterIsInstance<ShipCell>().map { it.ship }.distinct()

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
     *
     * @return the new board with the ship placed
     * @throws IllegalArgumentException if the ship cannot be placed
     */
    fun placeShip(ship: Ship): ConfigurableBoard {
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
    fun removeShip(ship: Ship): ConfigurableBoard =
        copy(
            grid = grid.replaceIf(
                predicate = { ship.coordinates.contains(it.coordinate) },
                new = { WaterCell(it.coordinate, it.wasHit) }
            )
        )

    companion object {

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
            ships: List<ShipType> = ShipType.defaults
        ) =
            ConfigurableBoard(size = size, grid = generateRandomMatrix(size, ships))

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
                                if (Ship.isValidShipCoordinate(
                                        coordinate = cell.coordinate,
                                        orientation = orientation,
                                        size = shipType.size,
                                        boardSize = size
                                    )
                                ) {
                                    val coordinates = Ship.getCoordinates(
                                        shipType = shipType,
                                        coordinate = cell.coordinate,
                                        orientation = orientation
                                    )

                                    if (
                                        coordinates.all { coordinate ->
                                            grid[coordinate.toIndex(size)] is WaterCell
                                        }
                                    ) acc + Ship(shipType, coordinates.first(), orientation)
                                    else acc
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
