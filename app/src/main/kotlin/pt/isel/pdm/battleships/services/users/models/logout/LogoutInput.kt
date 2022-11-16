package pt.isel.pdm.battleships.services.users.models.logout

/**
 * The Logout Input.
 *
 * @property refreshToken the refresh token of the user
 */
data class LogoutInput(
    val refreshToken: String
)
