package pt.isel.pdm.battleships.domain

/**
 * Represents the ship class in the game.
 *
 * @property size the size of the ship
 * @property shipName the name of the ship
 * @property points the points that the ship is worth
 */
enum class ShipType(val size: Int, val shipName: String, val points: Int) {
    CARRIER(5, "Carrier", 50),
    BATTLESHIP(4, "Battleship", 40),
    CRUISER(3, "Cruiser", 30),
    SUBMARINE(3, "Submarine", 30),
    DESTROYER(2, "Destroyer", 20)
}
