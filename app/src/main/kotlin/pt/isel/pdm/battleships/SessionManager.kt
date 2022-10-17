package pt.isel.pdm.battleships

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/**
 * Represents the session manager responsible for holding a user's session.
 *
 * @property token the user's token
 * @property username the user's username
 */
class SessionManager {
    private var _token: String? by mutableStateOf(null)
    private var _username: String? by mutableStateOf(null)

    val token
        get() = _token

    val username
        get() = _username

    /**
     * Checks if the user is logged in.
     *
     * @return true if the user is logged in, false otherwise
     */
    fun isLoggedIn() = _token != null

    /**
     * Updates the session with the given token and username.
     *
     * @param token the user's token
     * @param username the user's username
     */
    fun setSession(token: String, username: String) {
        this._token = token
        this._username = username
    }

    /**
     * Clears the session.
     */
    fun clearSession() {
        this._token = null
        this._username = null
    }
}
