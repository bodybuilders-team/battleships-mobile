package pt.isel.pdm.battleships.services.users.results

import pt.isel.pdm.battleships.services.dtos.ErrorDTO
import pt.isel.pdm.battleships.services.users.dtos.UserDTO

/**
 * The result of a get user by username request.
 */
sealed class UserResult {

    /**
     * The result of a successful get user by username request.
     *
     * @property user the user DTO
     */
    class Success(val user: UserDTO) : UserResult()

    /**
     * The request failed.
     *
     * @property error the error DTO
     */
    class Failure(val error: ErrorDTO) : UserResult()
}
