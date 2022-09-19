package pt.isel.pdm.battleships.domain.ship

import pt.isel.pdm.battleships.domain.board.Coordinate

/**
 * Represents a ship in the game.
 *
 * @property type the type of the ship
 * @property coordinate the coordinate of the ship
 * @property orientation the orientation of the ship
 * @property lives the number of lives of the ship
 *
 * @property coordinates list of coordinates occupied by the ship
 * @property isSunk true if the ship is sunk, false otherwise
 */
data class Ship(
    val type: ShipType,
    val coordinate: Coordinate,
    val orientation: Orientation,
    val lives: Int = type.size
) {
    val coordinates: List<Coordinate> = run {
        val coordinates = mutableListOf<Coordinate>()

        repeat(type.size) { i ->
            coordinates.add(
                Coordinate(
                    col = coordinate.col + if (orientation.isHorizontal()) i else 0,
                    row = coordinate.row + if (orientation.isVertical()) i else 0
                )
            )
        }

        coordinates
    }

    val isSunk = lives == 0
}
