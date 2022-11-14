package pt.isel.pdm.battleships.services.games.models.players.shot

import pt.isel.pdm.battleships.domain.games.shot.FiredShot
import pt.isel.pdm.battleships.services.games.models.CoordinateModel

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
    val result: ShotResultModel
) {

    /**
     * Converts this model to a Fired Shot.
     *
     * @return the fired shot
     */
    fun toFiredShot() = FiredShot(
        coordinate = coordinate.toCoordinate(),
        round = round,
        result = result.toShotResult()
    )
}
