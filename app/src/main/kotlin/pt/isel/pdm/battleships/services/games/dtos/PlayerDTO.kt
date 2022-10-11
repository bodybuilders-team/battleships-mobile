package pt.isel.pdm.battleships.services.games.dtos

/**
 * Represents a Player DTO.
 *
 * @property username the username of the player
 * @property points the points of the player
 */
data class PlayerDTO(
    val username: String,
    val points: Int
)
