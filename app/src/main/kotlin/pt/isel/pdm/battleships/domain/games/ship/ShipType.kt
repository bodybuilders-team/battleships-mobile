package pt.isel.pdm.battleships.domain.games.ship

/**
 * The ship class in the game.
 *
 * @property size the size of the ship
 * @property shipName the name of the ship
 * @property points the points that the ship is worth
 */
data class ShipType(
    val size: Int,
    val shipName: String,
    val points: Int = size * 10
) {
    companion object {
        const val CARRIER = "Carrier"
        const val BATTLESHIP = "Battleship"
        const val CRUISER = "Cruiser"
        const val SUBMARINE = "Submarine"
        const val DESTROYER = "Destroyer"

        val defaults = listOf(
            ShipType(size = 5, shipName = CARRIER),
            ShipType(size = 4, shipName = BATTLESHIP),
            ShipType(size = 3, shipName = CRUISER),
            ShipType(size = 3, shipName = SUBMARINE),
            ShipType(size = 2, shipName = DESTROYER)
        )
    }
}
