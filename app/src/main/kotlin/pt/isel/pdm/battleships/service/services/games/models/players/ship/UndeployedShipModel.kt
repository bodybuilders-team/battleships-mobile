package pt.isel.pdm.battleships.service.services.games.models.players.ship

import pt.isel.pdm.battleships.service.services.games.models.CoordinateModel

/**
 * The Undeployed Ship Model.
 *
 * @property type the ship type
 * @property coordinate the position
 * @property orientation the orientation
 */
data class UndeployedShipModel(
    val type: String,
    val coordinate: CoordinateModel,
    val orientation: String
)
