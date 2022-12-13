package pt.isel.pdm.battleships.domain.users

import pt.isel.pdm.battleships.R
import pt.isel.pdm.battleships.service.services.games.models.games.PlayerModel

/**
 * Information about a player.
 *
 * @param name the name of the player
 * @param avatarId the id of the avatar of the player
 * @param points the points of the player
 */
data class Player(
    val name: String,
    val points: Int,
    val avatarId: Int
) {
    constructor(playerModel: PlayerModel) : this(
        name = playerModel.username,
        points = playerModel.points,
        avatarId = R.drawable.ic_round_person_24
    )
}
