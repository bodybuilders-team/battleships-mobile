package pt.isel.pdm.battleships

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/**
 * Represents the session manager responsible for holding a user's session.
 *
 * @property _token the user's token
 */
class SessionManager {
    private var _token: String? by mutableStateOf(null)
    private var _username: String? by mutableStateOf(null)

    val token
        get() = _token

    val username
        get() = _username

    fun isLoggedIn() = _token != null

    fun setSession(token: String, username: String) {
        this._token = token
        this._username = username
    }

    fun clearSession() {
        this._token = null
        this._username = null
    }
}
