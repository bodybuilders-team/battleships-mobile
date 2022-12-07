package pt.isel.pdm.battleships.domain.users

/**
 * Information about a player.
 *
 * @param name the name of the player
 * @param avatarId the id of the avatar of the player
 * @param playerPoints the points of the player
 */
data class PlayerInfo(
    val name: String,
    val avatarId: Int,
    val playerPoints: Int
)
