package pt.isel.pdm.battleships.session

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/**
 * Responsible for holding a user's session, in memory.
 */
@Suppress("unused")
class SessionManagerInMemory : SessionManager {
    private var _accessToken: String? by mutableStateOf(null)
    private var _refreshToken: String? by mutableStateOf(null)
    private var _username: String? by mutableStateOf(null)
    private var _userHomeLink: String? by mutableStateOf(null)

    override val accessToken
        get() = _accessToken

    override val refreshToken
        get() = _refreshToken

    override val username
        get() = _username

    override val userHomeLink
        get() = _userHomeLink

    override fun setSession(
        accessToken: String,
        refreshToken: String,
        username: String,
        userHomeLink: String
    ) {
        this._accessToken = accessToken
        this._refreshToken = refreshToken
        this._username = username
        this._userHomeLink = userHomeLink
    }

    override fun clearSession() {
        this._accessToken = null
        this._refreshToken = null
        this._username = null
        this._userHomeLink = null
    }
}
