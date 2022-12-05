package pt.isel.pdm.battleships.ui.screens.authentication.register

import pt.isel.pdm.battleships.service.BattleshipsService
import pt.isel.pdm.battleships.session.SessionManager
import pt.isel.pdm.battleships.ui.screens.authentication.AuthenticationViewModel

/**
 * View model for the [RegisterActivity].
 *
 * @param sessionManager the manager used to handle the user session
 */
class RegisterViewModel(
    battleshipsService: BattleshipsService,
    sessionManager: SessionManager
) : AuthenticationViewModel(battleshipsService, sessionManager) {

    /**
     * Attempts to register the user with the given credentials.
     *
     * @param email the email of the user
     * @param username the username of the user
     * @param password the password of the user
     */
    fun register(email: String, username: String, password: String) {
        executeAuthenticationRequest(username = username) {
            battleshipsService.usersService.register(
                email = email,
                username = username,
                password = password
            )
        }
    }
}
