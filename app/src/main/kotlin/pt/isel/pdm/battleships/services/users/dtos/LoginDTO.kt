package pt.isel.pdm.battleships.services.users.dtos

/**
 * Represents a user login DTO.
 *
 * @property username the user's username
 * @property password the user's password
 */
data class LoginDTO(
    val username: String,
    val password: String
)
