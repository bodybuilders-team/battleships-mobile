package pt.isel.pdm.battleships.services.games.models.players.fireShots

import pt.isel.pdm.battleships.services.games.models.players.shot.UnfiredShotModel

/**
 * A Fire Shots DTO.
 *
 * @property shots the list of shots to be fired
 */
data class FireShotsInput(
    val shots: List<UnfiredShotModel>
)
