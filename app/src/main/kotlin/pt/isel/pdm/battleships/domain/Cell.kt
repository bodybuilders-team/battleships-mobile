package pt.isel.pdm.battleships.domain

import java.io.Serializable
import pt.isel.pdm.battleships.domain.board.Coordinate
import pt.isel.pdm.battleships.domain.ship.Ship

/**
 * Represents a cell in the board.
 *
 * @property coordinate the coordinate of the cell
 * @property wasHit if the cell was hit
 */
sealed class Cell(open val coordinate: Coordinate, open val wasHit: Boolean) : Serializable

/**
 * Represents an empty cell.
 */
data class WaterCell(
    override val coordinate: Coordinate,
    override val wasHit: Boolean
) :
    Cell(coordinate, wasHit), Serializable

/**
 * Represents a cell that contains a ship.
 *
 * @property ship the ship that is in this cell
 */
data class ShipCell(
    override val coordinate: Coordinate,
    override val wasHit: Boolean,
    val ship: Ship
) :
    Cell(coordinate, wasHit), Serializable
