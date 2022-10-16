package pt.isel.pdm.battleships.viewModels.authentication

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pt.isel.pdm.battleships.SessionManager
import pt.isel.pdm.battleships.services.users.UsersService

/**
 * Login view model for the login screen.
 *
 * @property sessionManager the session manager used to handle the user session
 * @property usersService the service used to handle the user's authentication
 */
class LoginViewModel(
    sessionManager: SessionManager,
    private val usersService: UsersService
) : AuthenticationViewModel(sessionManager) {

    /**
     * Attempts to login the user with the given credentials.
     *
     * @param username the username of the user
     * @param password the password of the user
     */
    fun login(username: String, password: String) {
        viewModelScope.launch {
            state = AuthenticationState.LOADING
            val res = usersService.login(username, password)
            updateState(username, res)
        }
    }
}
