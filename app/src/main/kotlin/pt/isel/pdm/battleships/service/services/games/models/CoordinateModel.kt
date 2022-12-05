package pt.isel.pdm.battleships.service.services.games.models

import pt.isel.pdm.battleships.domain.games.Coordinate

/**
 * The Coordinate Model.
 *
 * @property col the column of the coordinate
 * @property row the row of the coordinate
 */
data class CoordinateModel(
    val col: Char,
    val row: Int
) {

    /**
     * Converts the CoordinateModel to a Coordinate.
     *
     * @return the database model coordinate
     */
    fun toCoordinate() = Coordinate(col = col, row = row)
}
