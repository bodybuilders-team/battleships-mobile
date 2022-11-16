package pt.isel.pdm.battleships.domain.games.ship

import pt.isel.pdm.battleships.domain.games.Coordinate
import pt.isel.pdm.battleships.domain.games.board.Board
import pt.isel.pdm.battleships.services.games.models.players.ship.UndeployedShipModel

/**
 * A ship in the game.
 *
 * @property type the type of the ship
 * @property coordinate the coordinate of the ship
 * @property orientation the orientation of the ship
 *
 * @property coordinates list of coordinates occupied by the ship
 */
data class Ship(
    val type: ShipType,
    val coordinate: Coordinate,
    val orientation: Orientation
) {

    val coordinates: List<Coordinate> = getCoordinates(type, coordinate, orientation)

    /**
     * Converts the Ship to a UndeployedShipModel.
     *
     * @return the UndeployedShipModel
     */
    fun toUndeployedShipModel() = UndeployedShipModel(
        type = type.shipName,
        coordinate = coordinate.toCoordinateModel(),
        orientation = orientation.name
    )

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
        ): List<Coordinate> =
            (0 until shipType.size).map {
                when (orientation) {
                    Orientation.HORIZONTAL -> Coordinate(coordinate.col + it, coordinate.row)
                    Orientation.VERTICAL -> Coordinate(coordinate.col, coordinate.row + it)
                }
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
