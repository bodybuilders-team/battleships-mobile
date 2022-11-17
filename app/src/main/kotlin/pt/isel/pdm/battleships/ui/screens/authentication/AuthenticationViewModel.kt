package pt.isel.pdm.battleships.ui.screens.authentication

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import pt.isel.pdm.battleships.SessionManager
import pt.isel.pdm.battleships.services.BattleshipsService
import pt.isel.pdm.battleships.services.users.models.AuthenticationOutput
import pt.isel.pdm.battleships.services.utils.APIResult
import pt.isel.pdm.battleships.ui.screens.BattleshipsViewModel
import pt.isel.pdm.battleships.ui.screens.authentication.AuthenticationViewModel.AuthenticationState.IDLE
import pt.isel.pdm.battleships.ui.screens.authentication.AuthenticationViewModel.AuthenticationState.LINKS_LOADED
import pt.isel.pdm.battleships.ui.screens.authentication.AuthenticationViewModel.AuthenticationState.LOADING
import pt.isel.pdm.battleships.ui.screens.authentication.AuthenticationViewModel.AuthenticationState.SUCCESS
import pt.isel.pdm.battleships.ui.utils.launchAndExecuteRequest
import pt.isel.pdm.battleships.ui.utils.navigation.Links

/**
 * View model for both authentication methods (login and register).
 *
 * @property sessionManager the manager used to handle the user session
 *
 * @property _state the current state of the view model
 * @property events the events that occurred in the view model
 */
open class AuthenticationViewModel(
    battleshipsService: BattleshipsService,
    sessionManager: SessionManager
) : BattleshipsViewModel(battleshipsService, sessionManager) {

    private var _state: AuthenticationState by mutableStateOf(IDLE)
    val state
        get() = _state

    /**
     * Executes an authentication request.
     *
     * @param username the username of the user
     * @param getAuthenticationResult the result of the authentication process
     */
    protected fun executeAuthenticationRequest(
        username: String,
        getAuthenticationResult: suspend () -> APIResult<AuthenticationOutput>
    ) {
        check(_state == LINKS_LOADED) { "The view model is not in the links loaded state." }

        _state = LOADING

        launchAndExecuteRequest(
            request = { getAuthenticationResult() },
            events = _events,
            onFinish = { authenticationData ->
                if (authenticationData == null) {
                    _state = LINKS_LOADED
                    return@launchAndExecuteRequest
                }

                val properties = authenticationData.properties
                    ?: throw IllegalStateException("Token properties are null")

                sessionManager.setSession(
                    accessToken = properties.accessToken,
                    refreshToken = properties.refreshToken,
                    username = username
                )
                _state = SUCCESS
            }
        )
    }

    /**
     * Updates the links.
     *
     * @param links the links to update
     */
    override fun updateLinks(links: Links) {
        super.updateLinks(links)
        _state = LINKS_LOADED
    }

    /**
     * The state of an authentication process.
     *
     * @property LOADING the state of the authentication process while it is loading
     * @property SUCCESS the state of the authentication process when it is successful
     */
    enum class AuthenticationState {
        IDLE,
        LINKS_LOADED,
        LOADING,
        SUCCESS
    }
}
