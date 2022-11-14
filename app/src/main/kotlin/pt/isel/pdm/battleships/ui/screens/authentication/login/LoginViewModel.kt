package pt.isel.pdm.battleships.ui.screens.authentication.login

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pt.isel.pdm.battleships.SessionManager
import pt.isel.pdm.battleships.services.users.UsersService
import pt.isel.pdm.battleships.ui.screens.authentication.AuthenticationViewModel

/**
 * View model for the [LoginActivity].
 *
 * @property usersService the service used to handle the users
 * @param sessionManager the manager used to handle the user session
 */
class LoginViewModel(
    private val usersService: UsersService,
    sessionManager: SessionManager
) : AuthenticationViewModel(sessionManager) {

    /**
     * Attempts to login the user with the given credentials.
     *
     * @param loginLink the link to the login endpoint
     * @param username the username of the user
     * @param password the password of the user
     */
    fun login(loginLink: String, username: String, password: String) {
        viewModelScope.launch {
            state = AuthenticationState.LOADING

            updateState(username = username) {
                usersService.login(
                    loginLink = loginLink,
                    username = username,
                    password = password
                )
            }
        }
    }
}
