package pt.isel.pdm.battleships.viewModels.authentication

import android.util.Log
import androidx.lifecycle.viewModelScope
import java.io.IOException
import kotlinx.coroutines.launch
import pt.isel.pdm.battleships.SessionManager
import pt.isel.pdm.battleships.services.users.UsersService

/**
 * Represents the register view model for the register screen.
 *
 * @param sessionManager the session manager used to handle the user session
 * @property usersService the service used to handle the user's authentication
 */
class RegisterViewModel(
    sessionManager: SessionManager,
    private val usersService: UsersService
) : AuthenticationViewModel(sessionManager) {

    /**
     * Attempts to register the user with the given credentials.
     *
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
