package pt.isel.pdm.battleships.domain.ship

import pt.isel.pdm.battleships.domain.board.Board
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
    val coordinates: List<Coordinate> = getCoordinates(type, coordinate, orientation)

    val isSunk = lives == 0

    companion object {

        /**
         * Returns the list of coordinates occupied by a ship.
         *
         * @param shipType the type of the ship
         * @param coordinate the coordinate of the ship
         * @param orientation the orientation of the ship
         *
         * @return the list of coordinates occupied by the ship
         */
        fun getCoordinates(
            shipType: ShipType,
            coordinate: Coordinate,
            orientation: Orientation
        ): List<Coordinate> {
            val coordinates = mutableListOf<Coordinate>()

            repeat(shipType.size) { i ->
                val col = coordinate.col + if (orientation.isHorizontal()) i else 0
                val row = coordinate.row + if (orientation.isVertical()) i else 0

                if (col in Coordinate.COLS_RANGE && row in Coordinate.ROWS_RANGE) {
                    coordinates.add(Coordinate(col, row))
                } else {
                    throw IllegalArgumentException("Ship out of bounds")
                }
            }

            return coordinates
        }

        /**
         * Checks if the given coordinate is valid for the given ship information.
         *
         * @param col the column of the coordinate
         * @param row the row of the coordinate
         * @param orientation the orientation of the ship
         * @param size the size of the ship
         *
         * @return true if the coordinate is valid, false otherwise
         */
        fun isValidShipCoordinate(col: Int, row: Int, orientation: Orientation, size: Int) = (
            orientation == Orientation.HORIZONTAL &&
                (col until col + size)
                    .all { it in 0 until Board.BOARD_SIDE_LENGTH } &&
                row in 0 until Board.BOARD_SIDE_LENGTH
            ) || (
            orientation == Orientation.VERTICAL &&
                (row until row + size)
                    .all { it in 0 until Board.BOARD_SIDE_LENGTH } &&
                col in 0 until Board.BOARD_SIDE_LENGTH
            )
    }
}
