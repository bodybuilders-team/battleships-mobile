package pt.isel.pdm.battleships.domain.games

import pt.isel.pdm.battleships.domain.games.ship.Ship

/**
 * A cell in the board.
 *
 * @property coordinate the coordinate of the cell
 * @property wasHit if the cell was hit
 */
sealed class Cell(
    open val coordinate: Coordinate,
    open val wasHit: Boolean
)

/**
 * An empty cell.
 */
data class WaterCell(
    override val coordinate: Coordinate,
    override val wasHit: Boolean
) :
    Cell(coordinate, wasHit)

/**
 * A cell that contains a ship.
 *
 * @property ship the ship that is in this cell
 */
data class ShipCell(
    override val coordinate: Coordinate,
    override val wasHit: Boolean,
    val ship: Ship
) :
    Cell(coordinate, wasHit)

data class UnknownShipCell(
    override val coordinate: Coordinate,
    override val wasHit: Boolean
) :
    Cell(coordinate, wasHit)
