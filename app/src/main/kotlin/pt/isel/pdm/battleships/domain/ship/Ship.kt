package pt.isel.pdm.battleships.domain.ship

import pt.isel.pdm.battleships.domain.board.Coordinate
import pt.isel.pdm.battleships.ui.toPoint

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

    val position = run {
        val minIndex = coordinates.map {
            val point = it.toPoint()
            point.first + point.second
        }.withIndex().minByOrNull { it.value }?.index
            ?: throw IllegalStateException("Coordinates list must not be empty")
        coordinates[minIndex]
    }

    val orientation =
        if (coordinates[0].col == coordinates[1].col) {
            Orientation.VERTICAL
        } else Orientation.HORIZONTAL

    val isSunk = lives == 0
}
