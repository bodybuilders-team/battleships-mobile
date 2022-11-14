package pt.isel.pdm.battleships.services.users.models.login

/**
 * The Login Input.
 *
 * @property username the user's username
 * @property password the user's password
 */
data class LoginInput(
    val username: String,
    val password: String
)
