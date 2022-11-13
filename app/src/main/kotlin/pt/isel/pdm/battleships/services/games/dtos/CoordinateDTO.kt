package pt.isel.pdm.battleships.services.games.dtos

import pt.isel.pdm.battleships.domain.games.Coordinate

/**
 * Represents a Coordinate DTO.
 *
 * @property col the column of the coordinate
 * @property row the row of the coordinate
 */
data class CoordinateDTO(
    val col: Char,
    val row: Int
) {
    constructor(coordinate: Coordinate) : this(
        col = coordinate.col,
        row = coordinate.row
    )

    /**
     * Converts this DTO to a database model.
     *
     * @return the database model coordinate
     */
    fun toCoordinate() = Coordinate(col = col, row = row)
}
