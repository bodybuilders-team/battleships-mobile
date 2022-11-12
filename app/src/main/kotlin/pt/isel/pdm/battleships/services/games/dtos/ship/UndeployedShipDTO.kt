package pt.isel.pdm.battleships.services.games.dtos.ship

import pt.isel.pdm.battleships.services.games.dtos.CoordinateDTO

/**
 * Represents the undeployed ship DTO.
 *
 * @property type the ship type
 * @property coordinate the position
 * @property orientation the orientation
 */
data class UndeployedShipDTO(
    val type: String,
    val coordinate: CoordinateDTO,
    val orientation: String
)
