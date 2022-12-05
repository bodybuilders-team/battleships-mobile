package pt.isel.pdm.battleships.domain.games.board

import pt.isel.pdm.battleships.domain.games.Cell
import pt.isel.pdm.battleships.domain.games.ShipCell
import pt.isel.pdm.battleships.domain.games.UnknownShipCell
import pt.isel.pdm.battleships.domain.games.WaterCell
import pt.isel.pdm.battleships.domain.games.ship.Ship
import pt.isel.pdm.battleships.domain.games.shot.FiredShot
import pt.isel.pdm.battleships.domain.games.shot.ShotResult
import pt.isel.pdm.battleships.domain.utils.findValueByKey

/**
 * The opponent's board.
 *
 * @param size the size of the board
 * @param grid the grid of the board
 *
 * @property fleet the fleet of the board
 */
data class OpponentBoard(
    override val size: Int = DEFAULT_BOARD_SIZE,
    override val grid: List<Cell> = generateEmptyMatrix(size)
) : Board(size, grid) {

    init {
        isValid()
    }

    val fleet: List<Ship>
        get() = grid
            .filterIsInstance<ShipCell>()
            .map(ShipCell::ship)
            .distinct()

    /**
     * Shoots the [firedShots].
     * If the cell is already hit, the attack is invalid.
     * Otherwise, the cell becomes hit.
     *
     * @param firedShots coordinates to attack
     *
     * @return the board after the attack
     */
    fun updateWith(firedShots: List<FiredShot>): OpponentBoard {
        val sunkCoordinates = firedShots
            .mapNotNull { shot -> shot.sunkShip }
            .associateBy { it.coordinates }

        return copy(
            grid = grid.map { cell ->
                val firedShot = firedShots
                    .find { shot -> shot.coordinate == cell.coordinate }
                    ?: return@map cell

                check(!cell.wasHit) { "Cell at ${cell.coordinate} was already hit" }
                check(cell is WaterCell) { "Opponent cell can only be not hit if it is water cell" }

                sunkCoordinates
                    .findValueByKey { cell.coordinate in it }
                    ?.let { ShipCell(coordinate = cell.coordinate, wasHit = true, ship = it) }
                    ?: when (firedShot.result) {
                        ShotResult.HIT -> UnknownShipCell(
                            coordinate = cell.coordinate,
                            wasHit = true
                        )
                        else -> cell.copy(wasHit = true)
                    }
            }
        )
    }
}
