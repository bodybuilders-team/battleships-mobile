package pt.isel.pdm.battleships.domain

/**
 * Represents a ship in the game.
 *
 * @property type the type of the ship
 * @property coordinates the coordinates of the ship cells
 * @property lives the number of lives of the ship
 *
 * @property isSunk true if the ship is sunk, false otherwise
 */
data class Ship(
    val type: ShipType,
    val coordinates: List<Coordinate>,
    val lives: Int = type.size
) {
    init {
        require(coordinates.size == type.size) { "Invalid number of cells for ship type" }
    }

    val isSunk = lives == 0
}
