package pt.isel.pdm.battleships.domain

/**
 * Represents a cell in the board.
 */
sealed class Cell

/**
 * Represents a cell that has not been hit yet.
 */
class EmptyCell : Cell()

/**
 * Represents a cell that has been hit and contains a ship.
 *
 * @param ship the ship that is in this cell
 * @param wasHit true if the ship has been hit, false otherwise
 */
class ShipCell(val ship: Ship, val wasHit: Boolean) : Cell()

/**
 * Represents a cell that has been hit and does not contain a ship.
 */
class MissCell : Cell()
