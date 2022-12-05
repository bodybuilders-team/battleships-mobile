package pt.isel.pdm.battleships.session

import android.content.Context

/**
 * Session manager that uses shared preferences to store the session.
 *
 * @param context the application context
 *
 * @property accessToken the user's access token
 * @property refreshToken the user's refresh token
 * @property username the user's username
 * @property userHomeLink the user's home link
 */
class SessionManagerSharedPrefs(private val context: Context) : SessionManager {

    private val prefs by lazy {
        context.getSharedPreferences(SESSION_PREFS, Context.MODE_PRIVATE)
    }

    override val accessToken: String?
        get() = prefs.getString(ACCESS_TOKEN, null)

    override val refreshToken: String?
        get() = prefs.getString(REFRESH_TOKEN, null)

    override val username: String?
        get() = prefs.getString(USERNAME, null)

    override val userHomeLink: String?
        get() = prefs.getString(USER_HOME_LINK, null)

    override fun setSession(
        accessToken: String,
        refreshToken: String,
        username: String,
        userHomeLink: String
    ) {
        prefs.edit()
            .putString(ACCESS_TOKEN, accessToken)
            .putString(REFRESH_TOKEN, refreshToken)
            .putString(USERNAME, username)
            .putString(USER_HOME_LINK, userHomeLink)
            .apply()
    }

    override fun clearSession() {
        prefs.edit()
            .remove(ACCESS_TOKEN)
            .remove(REFRESH_TOKEN)
            .remove(USERNAME)
            .remove(USER_HOME_LINK)
            .apply()
    }

    companion object {
        private const val SESSION_PREFS = "session"
        private const val ACCESS_TOKEN = "accessToken"
        private const val REFRESH_TOKEN = "refreshToken"
        private const val USERNAME = "username"
        private const val USER_HOME_LINK = "userHomeLink"
    }
}
