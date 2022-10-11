package pt.isel.pdm.battleships.services.users.results

import pt.isel.pdm.battleships.services.dtos.ErrorDTO

/**
 * The result of an authentication attempt.
 */
sealed class AuthenticationResult {

    /**
     * The authentication was successful.
     *
     * @property token the authentication token
     */
    class Success(val token: String) : AuthenticationResult()

    /**
     * The game creation failed.
     *
     * @property error the error DTO
     */
    class Failure(val error: ErrorDTO) : AuthenticationResult()
}
