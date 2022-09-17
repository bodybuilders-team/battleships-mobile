package pt.isel.pdm.battleships.domain

/**
 * Represents the ship class in the game.
 *
 * @param size the size of the ship
 */
enum class ShipType(val size: Int) {
    CARRIER(5),
    BATTLESHIP(4),
    CRUISER(3),
    SUBMARINE(3),
    DESTROYER(2)
}
