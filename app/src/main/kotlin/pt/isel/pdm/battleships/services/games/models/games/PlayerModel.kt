package pt.isel.pdm.battleships.services.games.models.games

/**
 * A Player DTO.
 *
 * @property username the username of the player
 * @property points the points of the player
 */
data class PlayerModel(
    val username: String,
    val points: Int
)
