package pt.isel.pdm.battleships.ui.screens.authentication

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import pt.isel.pdm.battleships.SessionManager
import pt.isel.pdm.battleships.services.users.dtos.RegisterOutputDTO
import pt.isel.pdm.battleships.services.utils.HTTPResult
import pt.isel.pdm.battleships.ui.screens.authentication.AuthenticationViewModel.AuthenticationState.ERROR
import pt.isel.pdm.battleships.ui.screens.authentication.AuthenticationViewModel.AuthenticationState.IDLE
import pt.isel.pdm.battleships.ui.screens.authentication.AuthenticationViewModel.AuthenticationState.LOADING
import pt.isel.pdm.battleships.ui.screens.authentication.AuthenticationViewModel.AuthenticationState.SUCCESS

/**
 * View model for both authentication methods (login and register).
 *
 * @property sessionManager the manager used to handle the user session
 *
 * @property state the current state of the view model
 * @property errorMessage the error message to be displayed
 */
open class AuthenticationViewModel(
    private val sessionManager: SessionManager
) : ViewModel() {

    var state by mutableStateOf(IDLE)
    var errorMessage: String? by mutableStateOf(null)

    /**
     * Updates the state of an authentication view.
     *
     * @param username the username of the user
     * @param res the result of the authentication process
     */
    protected fun updateState(username: String, res: HTTPResult<RegisterOutputDTO>) {
        state = when (res) {
            is HTTPResult.Success -> {
                val properties = res.data.properties
                    ?: throw IllegalStateException("Token properties are null")

                sessionManager.setSession(token = properties.accessToken, username = username)
                SUCCESS
            }
            is HTTPResult.Failure -> {
                errorMessage = res.error.title
                ERROR
            }
        }
    }

    /**
     * Represents the state of an authentication process.
     *
     * @property IDLE the initial state of the authentication process
     * @property LOADING the state of the authentication process while it is loading
     * @property SUCCESS the state of the authentication process when it is successful
     * @property ERROR the state of the authentication process when an error occurs
     */
    enum class AuthenticationState {
        IDLE,
        LOADING,
        SUCCESS,
        ERROR
    }
}
