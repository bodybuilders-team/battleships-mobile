package pt.isel.pdm.battleships

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/**
 * The session manager responsible for holding a user's session.
 *
 * @property accessToken the user's access token
 * @property refreshToken the user's refresh token
 * @property username the user's username
 */
class SessionManager {
    private var _accessToken: String? by mutableStateOf(null)
    private var _refreshToken: String? by mutableStateOf(null)
    private var _username: String? by mutableStateOf(null)

    val accessToken
        get() = _accessToken

    val refreshToken
        get() = _refreshToken

    val username
        get() = _username

    /**
     * Checks if the user is logged in.
     *
     * @return true if the user is logged in, false otherwise
     */
    fun isLoggedIn() = _accessToken != null

    /**
     * Updates the session with the given tokens and username.
     *
     * @param accessToken the user's access token
     * @param refreshToken the user's refresh token
     * @param username the user's username
     */
    fun setSession(accessToken: String, refreshToken: String, username: String) {
        this._accessToken = accessToken
        this._refreshToken = refreshToken
        this._username = username
    }

    /**
     * Clears the session.
     */
    fun clearSession() {
        this._accessToken = null
        this._refreshToken = null
        this._username = null
    }
}
