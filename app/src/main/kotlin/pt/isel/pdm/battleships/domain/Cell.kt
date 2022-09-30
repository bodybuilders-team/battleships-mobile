package pt.isel.pdm.battleships.domain

import pt.isel.pdm.battleships.domain.board.Coordinate
import pt.isel.pdm.battleships.domain.ship.Ship

/**
 * Represents a cell in the board.
 *
 * @property coordinate the coordinate of the cell
 */
sealed class Cell(open val coordinate: Coordinate, open val wasHit: Boolean)

/**
 * Represents an empty cell.
 */
data class WaterCell(
    override val coordinate: Coordinate,
    override val wasHit: Boolean
) :
    Cell(coordinate, wasHit)

/**
 * Represents a cell that contains a ship.
 *
 * @property ship the ship that is in this cell
 * @property wasHit true if the ship has been hit, false otherwise
 */
data class ShipCell(
    override val coordinate: Coordinate,
    override val wasHit: Boolean,
    val ship: Ship
) :
    Cell(coordinate, wasHit)
