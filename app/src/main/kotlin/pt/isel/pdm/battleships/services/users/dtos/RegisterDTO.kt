package pt.isel.pdm.battleships.services.users.dtos

/**
 * Represents a user registration DTO.
 *
 * @property username the user's username
 * @property email the user's email
 * @property password the user's password
 */
data class RegisterDTO(
    val username: String,
    val email: String,
    val password: String
)
