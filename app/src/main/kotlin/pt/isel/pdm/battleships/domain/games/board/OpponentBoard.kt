package pt.isel.pdm.battleships.domain.games.board

import pt.isel.pdm.battleships.domain.exceptions.InvalidShotException
import pt.isel.pdm.battleships.domain.games.Cell
import pt.isel.pdm.battleships.domain.games.ShipCell
import pt.isel.pdm.battleships.domain.games.UnknownShipCell
import pt.isel.pdm.battleships.domain.games.WaterCell
import pt.isel.pdm.battleships.domain.games.ship.Ship
import pt.isel.pdm.battleships.domain.games.shot.FiredShot
import pt.isel.pdm.battleships.domain.games.shot.ShotResult

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
     * @throws InvalidShotException if the attack is invalid
     */
    fun updateWith(firedShots: List<FiredShot>): OpponentBoard =
        copy(
            grid = grid.map { cell ->
                val firedShot = firedShots
                    .find { shot -> shot.coordinate == cell.coordinate }
                    ?: return@map cell

                if (cell.wasHit) throw InvalidShotException("Cell already hit")

                check(cell is ShipCell || cell is UnknownShipCell) {
                    "Cell cannot be ShipCell or UnknownShipCell if it was not hit"
                }
                cell as WaterCell

                when (firedShot.result) {
                    ShotResult.HIT -> UnknownShipCell(
                        coordinate = cell.coordinate,
                        wasHit = true
                    )
                    ShotResult.SUNK -> {
                        checkNotNull(firedShot.sunkShip) { "Sunk ship cannot be null" }

                        grid.forEach { cell2 ->
                            if (cell2.coordinate in firedShot.sunkShip.coordinates) {
                                if (cell2 !is UnknownShipCell)
                                    throw InvalidShotException("Cell is not a ship cell")

                                return@map ShipCell(
                                    coordinate = cell2.coordinate,
                                    wasHit = true,
                                    ship = firedShot.sunkShip
                                )
                            }
                        }

                        ShipCell(
                            coordinate = cell.coordinate,
                            wasHit = true,
                            ship = firedShot.sunkShip
                        )
                    }
                    else -> cell.copy(wasHit = true)
                }
            }
        )
}
