package pt.isel.pdm.battleships.services.games.dtos.shot

import pt.isel.pdm.battleships.domain.games.shot.FiredShot
import pt.isel.pdm.battleships.services.games.dtos.CoordinateDTO

/**
 * The DTO of a fired shot.
 *
 * @property coordinate the coordinate of the shot
 * @property round the round in which the shot was made
 * @property result the result of the shot
 */
data class FiredShotDTO(
    val coordinate: CoordinateDTO,
    val round: Int,
    val result: ShotResultDTO
) {
    fun toFiredShot() = FiredShot(
        coordinate = coordinate.toCoordinate(),
        round = round,
        result = result.toShotResult()
    )
}
