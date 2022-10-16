package pt.isel.pdm.battleships.viewModels.authentication

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import pt.isel.pdm.battleships.SessionManager
import pt.isel.pdm.battleships.services.users.dtos.TokenDTO
import pt.isel.pdm.battleships.services.utils.Result

/**
 * Represents the ViewModel for both authentication screens (login and register).
 *
 * @property sessionManager the session manager used to handle the user's session
 * @property state the current state of the authentication process
 * @property errorMessage the error message to be displayed in case of an error
 */
open class AuthenticationViewModel(
    private val sessionManager: SessionManager
) : ViewModel() {
    var state by mutableStateOf(AuthenticationState.IDLE)
    var errorMessage: String? by mutableStateOf(null)

    /**
     * Updates the state of an authentication view.
     *
     * @param res the result of the authentication process
     */
    protected fun updateState(username: String, res: Result<TokenDTO>) {
        state = when (res) {
            is Result.Success -> {
                val properties =
                    res.data.properties ?: throw IllegalStateException("Token properties are null")
                sessionManager.setSession(properties.token, username)
                AuthenticationState.SUCCESS
            }
            is Result.Failure -> {
                errorMessage = res.error.message
                AuthenticationState.ERROR
            }
        }
    }
}
