package pt.isel.pdm.battleships.domain

/**
 * Represents a ship in the game.
 *
 * @param shipType the type of the ship
 * @param coordinates the coordinates of the ship cells
 * @param orientation the orientation of the ship
 */
class Ship(
    val shipType: ShipType,
    val coordinates: List<Coordinate>,
    val orientation: Orientation
)
