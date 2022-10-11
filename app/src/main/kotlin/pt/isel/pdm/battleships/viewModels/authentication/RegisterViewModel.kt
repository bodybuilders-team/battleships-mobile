package pt.isel.pdm.battleships.viewModels.authentication

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pt.isel.pdm.battleships.SessionManager
import pt.isel.pdm.battleships.service.UserService

/**
 * Represents the register view model for the register screen.
 *
 * @property sessionManager The session manager used to handle the user session.
 * @property usersService The service used to handle the user's authentication.
 */
class RegisterViewModel(
    sessionManager: SessionManager,
    private val usersService: UserService
) : AuthenticationViewModel(sessionManager) {

    /**
     * Attempts to register the user with the given credentials.
     */
    fun register(email: String, username: String, password: String) {
        viewModelScope.launch {
            state = AuthenticationState.LOADING
            val res = usersService.register(email, username, password)
            updateState(res)
        }
    }
}
