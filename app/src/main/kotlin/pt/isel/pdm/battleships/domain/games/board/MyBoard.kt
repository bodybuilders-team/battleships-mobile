package pt.isel.pdm.battleships.domain.games.board

import pt.isel.pdm.battleships.domain.games.Cell
import pt.isel.pdm.battleships.domain.games.Coordinate
import pt.isel.pdm.battleships.domain.games.ShipCell
import pt.isel.pdm.battleships.domain.games.UnknownShipCell
import pt.isel.pdm.battleships.domain.games.WaterCell
import pt.isel.pdm.battleships.domain.games.ship.Ship
import pt.isel.pdm.battleships.domain.utils.replaceIf

/**
 * A board of the player.
 *
 * @property size the size of the board
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

    val fleet: List<Ship> = grid
        .filterIsInstance<ShipCell>()
        .map(ShipCell::ship)
        .distinct()

    /**
     * Shoots the [firedCoordinates].
     * If the cell is already hit, the attack is invalid.
     * Otherwise, the cell becomes hit.
     *
     * @param firedCoordinates coordinates to attack
     *
     * @return the board after the attack
     */
    fun shoot(firedCoordinates: List<Coordinate>): MyBoard =
        copy(
            grid = grid
                .replaceIf(predicate = { cell -> cell.coordinate in firedCoordinates }) { cell ->
                    check(!cell.wasHit) { "Cell at ${cell.coordinate} was already hit" }

                    when (cell) {
                        is ShipCell -> cell.copy(wasHit = true)
                        is WaterCell -> cell.copy(wasHit = true)
                        is UnknownShipCell -> throw IllegalStateException(
                            "UnknownShipCell should not be present in MyBoard"
                        )
                    }
                }
        )

    companion object {

        /**
         * Creates a new instance of [MyBoard] with the given size and fleet.
         *
         * @param size the size of the board
         * @param initialFleet the initial fleet of the board
         *
         * @return instance of [MyBoard]
         */
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
}
