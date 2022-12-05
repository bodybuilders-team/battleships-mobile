package pt.isel.pdm.battleships.session

/**
 * Responsible for holding a user's session.
 *
 * @property accessToken the user's access token
 * @property refreshToken the user's refresh token
 * @property username the user's username
 * @property userHomeLink the user's home link
 */
interface SessionManager {
    val accessToken: String?
    val refreshToken: String?
    val username: String?
    val userHomeLink: String?

    /**
     * Checks if the user is logged in.
     *
     * @return true if the user is logged in, false otherwise
     */
    fun isLoggedIn(): Boolean = accessToken != null

    /**
     * Updates the session with the given tokens and username.
     *
     * @param accessToken the user's access token
     * @param refreshToken the user's refresh token
     * @param username the user's username
     * @param userHomeLink the user's home link
     */
    fun setSession(
        accessToken: String,
        refreshToken: String,
        username: String,
        userHomeLink: String
    )

    /**
     * Clears the session.
     */
    fun clearSession()
}
