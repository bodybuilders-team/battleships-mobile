package pt.isel.pdm.battleships.domain.users

/**
 * A user of the application.
 *
 * @property username the user's username
 * @property email the user's email
 * @property points the user's points
 * @property numberOfGamesPlayed the number of games played by the user
 */
data class User(
    val username: String,
    val email: String,
    val points: Int,
    val numberOfGamesPlayed: Int
)
