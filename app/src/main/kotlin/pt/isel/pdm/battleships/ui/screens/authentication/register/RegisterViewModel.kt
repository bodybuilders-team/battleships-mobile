package pt.isel.pdm.battleships.ui.screens.authentication.register

import android.util.Log
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pt.isel.pdm.battleships.SessionManager
import pt.isel.pdm.battleships.services.users.UsersService
import pt.isel.pdm.battleships.ui.screens.authentication.AuthenticationViewModel
import java.io.IOException

/**
 * View model for the RegisterActivity.
 *
 * @property usersService the service used to handle the users
 * @param sessionManager the manager used to handle the user session
 */
class RegisterViewModel(
    private val usersService: UsersService,
    sessionManager: SessionManager
) : AuthenticationViewModel(sessionManager) {

    /**
     * Attempts to register the user with the given credentials.
     *
     * @param registerLink the link to the register endpoint
     * @param email the email of the user
     * @param username the username of the user
     * @param password the password of the user
     */
    fun register(registerLink: String, email: String, username: String, password: String) {
        viewModelScope.launch {
            try {
                state = AuthenticationState.LOADING
                val res = usersService.register(
                    registerLink = registerLink,
                    email = email,
                    username = username,
                    password = password
                )
                updateState(username = username, res = res)
            } catch (e: IOException) {
                Log.v("RegisterViewModel", "Failed to register user", e)
            }
        }
    }
}
