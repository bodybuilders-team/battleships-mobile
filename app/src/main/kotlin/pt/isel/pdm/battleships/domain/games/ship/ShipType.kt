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
        const val CARRIER_NAME = "Carrier"
        const val BATTLESHIP_NAME = "Battleship"
        const val CRUISER_NAME = "Cruiser"
        const val SUBMARINE_NAME = "Submarine"
        const val DESTROYER_NAME = "Destroyer"

        val CARRIER = ShipType(size = 5, shipName = CARRIER_NAME)
        val BATTLESHIP = ShipType(size = 4, shipName = BATTLESHIP_NAME)
        val CRUISER = ShipType(size = 3, shipName = CRUISER_NAME)
        val SUBMARINE = ShipType(size = 3, shipName = SUBMARINE_NAME)
        val DESTROYER = ShipType(size = 2, shipName = DESTROYER_NAME)

        val defaults = listOf(CARRIER, BATTLESHIP, CRUISER, SUBMARINE, DESTROYER)

        val defaultsMap
            get() = defaults.associateWith { 1 }
    }
}
