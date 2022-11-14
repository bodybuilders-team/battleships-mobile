package pt.isel.pdm.battleships.ui.screens.authentication

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import pt.isel.pdm.battleships.SessionManager
import pt.isel.pdm.battleships.services.users.models.AuthenticationOutput
import pt.isel.pdm.battleships.services.utils.APIResult
import pt.isel.pdm.battleships.ui.screens.authentication.AuthenticationViewModel.AuthenticationState.IDLE
import pt.isel.pdm.battleships.ui.screens.authentication.AuthenticationViewModel.AuthenticationState.LOADING
import pt.isel.pdm.battleships.ui.screens.authentication.AuthenticationViewModel.AuthenticationState.SUCCESS
import pt.isel.pdm.battleships.ui.utils.Event
import pt.isel.pdm.battleships.ui.utils.handle
import pt.isel.pdm.battleships.ui.utils.navigation.Rels.USER_HOME
import pt.isel.pdm.battleships.ui.utils.tryExecuteHttpRequest

/**
 * View model for both authentication methods (login and register).
 *
 * @property sessionManager the manager used to handle the user session
 *
 * @property state the current state of the view model
 * @property link the link to the user home screen
 * @property events the events that occurred in the view model
 */
open class AuthenticationViewModel(
    private val sessionManager: SessionManager
) : ViewModel() {

    var state by mutableStateOf(IDLE)
    var link by mutableStateOf<String?>(null)

    private val _events = MutableSharedFlow<Event>()
    val events: SharedFlow<Event> = _events

    /**
     * Updates the state of an authentication view.
     *
     * @param username the username of the user
     * @param getAuthenticationResult the result of the authentication process
     */
    protected suspend fun updateState(
        username: String,
        getAuthenticationResult: suspend () -> APIResult<AuthenticationOutput>
    ) {
        check(state == LOADING) { "The view model is not in the loading state" }

        val httpRes = tryExecuteHttpRequest {
            getAuthenticationResult()
        }

        val res = httpRes.handle(
            events = _events,
            onFailure = { state = IDLE }
        ) ?: return

        res.handle(
            events = _events,
            onSuccess = { authenticationData ->
                val properties = authenticationData.properties
                    ?: throw IllegalStateException("Token properties are null")

                link = authenticationData.links
                    ?.find { it.rel.contains(USER_HOME) }?.href?.path
                    ?: throw IllegalStateException("User home link not found")

                sessionManager.setSession(
                    accessToken = properties.accessToken,
                    refreshToken = properties.refreshToken,
                    username = username
                )
                state = SUCCESS
            },
            onFailure = { state = IDLE }
        )
    }

    /**
     * The state of an authentication process.
     *
     * @property IDLE the initial state of the authentication process
     * @property LOADING the state of the authentication process while it is loading
     * @property SUCCESS the state of the authentication process when it is successful
     */
    enum class AuthenticationState {
        IDLE,
        LOADING,
        SUCCESS
    }
}
