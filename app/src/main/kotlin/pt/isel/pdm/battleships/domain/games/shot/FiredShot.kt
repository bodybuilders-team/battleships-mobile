package pt.isel.pdm.battleships.domain.games.shot

import pt.isel.pdm.battleships.domain.games.Coordinate
import pt.isel.pdm.battleships.domain.games.ship.Ship

/**
 * A shot fired by a player.
 *
 * @property coordinate the coordinate of the shot
 * @property round the round in which the shot was fired
 * @property result the result of the shot
 * @property sunkShip the ship that was sunk by the shot, or null if the shot didn't sink any ship
 */
data class FiredShot(
    val coordinate: Coordinate,
    val round: Int,
    val result: ShotResult,
    val sunkShip: Ship?
)
