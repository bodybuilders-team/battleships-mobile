package pt.isel.pdm.battleships.ui.screens.authentication

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import pt.isel.pdm.battleships.SessionManager
import pt.isel.pdm.battleships.services.users.dtos.AuthenticationOutputDTO
import pt.isel.pdm.battleships.services.utils.APIResult
import pt.isel.pdm.battleships.ui.screens.authentication.AuthenticationViewModel.AuthenticationState.IDLE
import pt.isel.pdm.battleships.ui.screens.authentication.AuthenticationViewModel.AuthenticationState.LOADING
import pt.isel.pdm.battleships.ui.screens.authentication.AuthenticationViewModel.AuthenticationState.SUCCESS
import pt.isel.pdm.battleships.ui.utils.HTTPResult
import pt.isel.pdm.battleships.ui.utils.tryExecuteHttpRequest

/**
 * View model for both authentication methods (login and register).
 *
 * @property sessionManager the manager used to handle the user session
 *
 * @property state the current state of the view model
 */
open class AuthenticationViewModel(
    private val sessionManager: SessionManager
) : ViewModel() {

    var state by mutableStateOf(IDLE)
    var link by mutableStateOf<String?>(null)

    private val _events = MutableSharedFlow<AuthenticationEvent>()
    val events: SharedFlow<AuthenticationEvent> = _events

    /**
     * Updates the state of an authentication view.
     *
     * @param username the username of the user
     * @param getAuthenticationResult the result of the authentication process
     */
    protected suspend fun updateState(
        username: String,
        getAuthenticationResult: suspend () -> APIResult<AuthenticationOutputDTO>
    ) {
        val httpRes = tryExecuteHttpRequest {
            getAuthenticationResult()
        }

        val res = when (httpRes) {
            is HTTPResult.Success -> httpRes.data
            is HTTPResult.Failure -> {
                _events.emit(AuthenticationEvent.Error(httpRes.error))
                state = IDLE
                return
            }
        }

        when (res) {
            is APIResult.Success -> {
                val properties = res.data.properties
                    ?: throw IllegalStateException("Token properties are null")

                link = res.data.links?.find { it.rel.contains("user-home") }?.href?.path
                    ?: throw IllegalStateException("User home link not found")

                sessionManager.setSession(
                    accessToken = properties.accessToken,
                    refreshToken = properties.refreshToken,
                    username = username
                )
                state = SUCCESS
            }
            is APIResult.Failure -> {
                _events.emit(AuthenticationEvent.Error(res.error.title))
                state = IDLE
                return
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
        SUCCESS
    }

    /**
     * Represents the events that can be emitted.
     */
    sealed class AuthenticationEvent {
        class Error(val message: String) : AuthenticationEvent()
    }
}
