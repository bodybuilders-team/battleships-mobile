package pt.isel.pdm.battleships.services.games.models.players.shot

import pt.isel.pdm.battleships.domain.games.shot.ShotResult

/**
 * The DTO of a shot result.
 *
 * @property result the result of the shot
 */
data class ShotResultModel(
    val result: String
) {
    fun toShotResult() = ShotResult(
        result = result
    )
}
