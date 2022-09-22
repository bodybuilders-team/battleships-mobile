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

                coordinates.add(Coordinate(col, row))
            }

            return coordinates
        }

        /**
         * Checks if the given coordinate is valid for the given ship information.
         *
         * @param coordinate the coordinate
         * @param orientation the orientation of the ship
         * @param size the size of the ship
         * @param boardSize the size of the grid
         *
         * @return true if the coordinate is valid, false otherwise
         */
        fun isValidShipCoordinate(
            coordinate: Coordinate,
            orientation: Orientation,
            size: Int,
            boardSize: Int
        ): Boolean {
            val colsRange = Board.getColumnsRange(boardSize)
            val rowsRange = Board.getRowsRange(boardSize)

            return (
                orientation == Orientation.HORIZONTAL &&
                    (coordinate.col + size - 1) in colsRange &&
                    coordinate.row in rowsRange
                ) || (
                orientation == Orientation.VERTICAL &&
                    (coordinate.row + size - 1) in rowsRange &&
                    coordinate.col in colsRange
                )
        }
    }
}
