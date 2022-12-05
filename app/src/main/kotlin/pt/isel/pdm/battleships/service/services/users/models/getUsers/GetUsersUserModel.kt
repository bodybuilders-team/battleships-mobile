package pt.isel.pdm.battleships.service.services.users.models.getUsers

/**
 * A Get Users User Model.
 *
 * @property username the username of the user
 * @property email the email of the user
 * @property points the points of the user
 * @property numberOfGamesPlayed the number of games played by the user
 */
data class GetUsersUserModel(
    val username: String,
    val email: String,
    val points: Int,
    val numberOfGamesPlayed: Int
)
