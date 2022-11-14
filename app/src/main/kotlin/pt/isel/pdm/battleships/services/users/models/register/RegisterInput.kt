package pt.isel.pdm.battleships.services.users.models.register

/**
 * The Register Input.
 *
 * @property username the user's username
 * @property email the user's email
 * @property password the user's password
 */
data class RegisterInput(
    val username: String,
    val email: String,
    val password: String
)
