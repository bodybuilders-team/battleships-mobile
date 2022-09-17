package pt.isel.pdm.battleships.domain

/**
 * Represents a cell in the board.
 *
 * @property coordinate the coordinate of the cell
 */
sealed class Cell(open val coordinate: Coordinate)

/**
 * Represents a cell that has not been hit yet.
 */
data class WaterCell(override val coordinate: Coordinate) : Cell(coordinate)

/**
 * Represents a cell that contains a ship.
 *
 * @property ship the ship that is in this cell
 * @property wasHit true if the ship has been hit, false otherwise
 */
data class ShipCell(
    override val coordinate: Coordinate,
    val ship: Ship,
    val wasHit: Boolean = false
) :
    Cell(coordinate)

/**
 * Represents a cell that has been hit and does not contain a ship.
 */
data class MissCell(override val coordinate: Coordinate) : Cell(coordinate)
