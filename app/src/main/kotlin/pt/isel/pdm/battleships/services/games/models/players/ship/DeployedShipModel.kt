package pt.isel.pdm.battleships.services.games.models.players.ship

import pt.isel.pdm.battleships.services.games.models.CoordinateModel

/**
 * The Deployed Ship Model.
 *
 * @property type the ship type
 * @property coordinate the position
 * @property orientation the orientation
 * @property lives the number of lives
 */
data class DeployedShipModel(
    val type: String,
    val coordinate: CoordinateModel,
    val orientation: String,
    val lives: Int
)
