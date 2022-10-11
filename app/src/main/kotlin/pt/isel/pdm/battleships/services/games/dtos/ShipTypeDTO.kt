package pt.isel.pdm.battleships.services.games.dtos

/**
 * Represents a Ship Type DTO.
 *
 * @property shipName the name of the ship
 * @property size the size of the ship
 * @property quantity the quantity of ships of this type
 * @property points the points that the ship is worth
 */
data class ShipTypeDTO(
    val shipName: String,
    val size: Int,
    val quantity: Int,
    val points: Int
)
