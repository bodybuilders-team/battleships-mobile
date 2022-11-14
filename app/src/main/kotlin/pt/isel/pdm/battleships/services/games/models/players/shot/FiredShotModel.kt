package pt.isel.pdm.battleships.services.games.models.players.shot

import pt.isel.pdm.battleships.domain.games.shot.FiredShot
import pt.isel.pdm.battleships.services.games.models.CoordinateModel

/**
 * The DTO of a fired shot.
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
    fun toFiredShot() = FiredShot(
        coordinate = coordinate.toCoordinate(),
        round = round,
        result = result.toShotResult()
    )
}
