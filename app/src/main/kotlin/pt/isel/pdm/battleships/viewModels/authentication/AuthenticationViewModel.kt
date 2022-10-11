package pt.isel.pdm.battleships.viewModels.authentication

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import pt.isel.pdm.battleships.SessionManager
import pt.isel.pdm.battleships.service.AuthenticationResult

enum class AuthenticationState {
    IDLE,
    LOADING,
    SUCCESS,
    ERROR
}

/**
 * Represents the ViewModel for both authentication screens (login and register).
 *
 * @property sessionManager The session manager used to handle the user's session.
 */
open class AuthenticationViewModel(
    private val sessionManager: SessionManager
) : ViewModel() {
    var state by mutableStateOf(AuthenticationState.IDLE)
    var errorMessage: String? by mutableStateOf(null)

    /**
     * Updates the state of an authentication view.
     *
     * @param res The result of the authentication process.
     */
    protected fun updateState(res: AuthenticationResult) = when (res) {
        is AuthenticationResult.Success -> {
            sessionManager.token = res.token
            state = AuthenticationState.SUCCESS
        }
        is AuthenticationResult.Failure -> {
            errorMessage = res.message
            state = AuthenticationState.ERROR
        }
    }
}
