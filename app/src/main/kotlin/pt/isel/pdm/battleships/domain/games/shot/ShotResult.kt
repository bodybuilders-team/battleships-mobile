package pt.isel.pdm.battleships.domain.games.shot

/**
 * The possible results of a shot.
 *
 * @property HIT the shot hit a ship
 * @property MISS the shot missed and hit the water
 * @property SUNK the shot sunk a ship
 */
enum class ShotResult {
    HIT,
    MISS,
    SUNK
}
