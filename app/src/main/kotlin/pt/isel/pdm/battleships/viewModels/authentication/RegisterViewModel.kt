package pt.isel.pdm.battleships.viewModels.authentication

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pt.isel.pdm.battleships.SessionManager
import pt.isel.pdm.battleships.services.users.UsersService

/**
 * Represents the register view model for the register screen.
 *
 * @property sessionManager the session manager used to handle the user session
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
    fun register(email: String, username: String, password: String) {
        viewModelScope.launch {
            state = AuthenticationState.LOADING
            val res = usersService.register(email, username, password)
            updateState(res)
        }
    }
}
