package pt.isel.pdm.battleships.services.games.dtos.shot

import pt.isel.pdm.battleships.domain.games.shot.ShotResult

/**
 * The DTO of a shot result.
 *
 * @property result the result of the shot
 */
data class ShotResultDTO(
    val result: String
) {
    fun toShotResult() = ShotResult(
        result = result
    )
}
