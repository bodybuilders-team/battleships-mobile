package pt.isel.pdm.battleships.services.games.models.players.shot

import pt.isel.pdm.battleships.domain.games.ship.Orientation
import pt.isel.pdm.battleships.domain.games.ship.Ship
import pt.isel.pdm.battleships.domain.games.ship.ShipType
import pt.isel.pdm.battleships.domain.games.shot.FiredShot
import pt.isel.pdm.battleships.services.games.models.CoordinateModel
import pt.isel.pdm.battleships.services.games.models.players.ship.DeployedShipModel

/**
 * The Fired Shot Model.
 *
 * @property coordinate the coordinate of the shot
 * @property round the round in which the shot was made
 * @property result the result of the shot
 */
data class FiredShotModel(
    val coordinate: CoordinateModel,
    val round: Int,
    val result: ShotResultModel,
    val sunkShip: DeployedShipModel?
) {

    /**
     * Converts this model to a Fired Shot.
     *
     * @return the fired shot
     */
    fun toFiredShot(shipTypes: Map<ShipType, Int>) = FiredShot(
        coordinate = coordinate.toCoordinate(),
        round = round,
        result = result.toShotResult(),
        sunkShip = sunkShip?.let {
            Ship(
                type = shipTypes.keys
                    .find { shipType -> shipType.shipName == sunkShip.type }
                    ?: throw IllegalStateException("Invalid ship type"),
                coordinate = sunkShip.coordinate.toCoordinate(),
                orientation = Orientation.valueOf(sunkShip.orientation)
            )
        }
    )
}
