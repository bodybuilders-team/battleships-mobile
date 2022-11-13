package pt.isel.pdm.battleships.domain.games.shot

import pt.isel.pdm.battleships.domain.games.Coordinate

/**
 * A shot fired by a player.
 *
 * @property coordinate the coordinate of the shot
 * @property round the round in which the shot was fired
 * @property result the result of the shot
 */
data class FiredShot(
    val coordinate: Coordinate,
    val round: Int,
    val result: ShotResult
)
