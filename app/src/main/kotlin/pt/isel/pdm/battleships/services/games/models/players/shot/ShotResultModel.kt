package pt.isel.pdm.battleships.services.games.models.players.shot

import pt.isel.pdm.battleships.domain.games.shot.ShotResult

/**
 * The Shot Result Model.
 *
 * @property result the result of the shot
 */
data class ShotResultModel(
    val result: String
) {

    /**
     * Converts this model to a Shot Result.
     *
     * @return the shot result
     */
    fun toShotResult() = ShotResult.valueOf(result)
}
