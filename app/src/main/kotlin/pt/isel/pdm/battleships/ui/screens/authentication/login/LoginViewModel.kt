package pt.isel.pdm.battleships.ui.screens.authentication.login

import pt.isel.pdm.battleships.service.BattleshipsService
import pt.isel.pdm.battleships.session.SessionManager
import pt.isel.pdm.battleships.ui.screens.authentication.AuthenticationViewModel

/**
 * View model for the [LoginActivity].
 *
 * @param sessionManager the manager used to handle the user session
 */
class LoginViewModel(
    battleshipsService: BattleshipsService,
    sessionManager: SessionManager
) : AuthenticationViewModel(battleshipsService, sessionManager) {

    /**
     * Attempts to login the user with the given credentials.
     *
     * @param username the username of the user
     * @param password the password of the user
     */
    fun login(username: String, password: String) {
        executeAuthenticationRequest(username = username) {
            battleshipsService.usersService.login(
                username = username,
                password = password
            )
        }
    }
}
