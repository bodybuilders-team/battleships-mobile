package pt.isel.pdm.battleships.viewModels.authentication

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pt.isel.pdm.battleships.SessionManager
import pt.isel.pdm.battleships.service.UserService

/**
 * Login view model for the login screen.
 *
 * @property sessionManager The session manager used to handle the user session.
 * @property usersService The service used to handle the user's authentication.
 */
class LoginViewModel(
    sessionManager: SessionManager,
    private val usersService: UserService
) : AuthenticationViewModel(sessionManager) {

    /**
     * Attempts to login the user with the given credentials.
     */
    fun login(username: String, password: String) {
        viewModelScope.launch {
            state = AuthenticationState.LOADING
            val res = usersService.login(username, password)
            updateState(res)
        }
    }
}
