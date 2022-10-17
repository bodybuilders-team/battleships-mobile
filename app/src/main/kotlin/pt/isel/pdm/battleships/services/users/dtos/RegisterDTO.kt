package pt.isel.pdm.battleships.services.users.dtos

/**
 * Represents a user registration DTO.
 *
 * @property email the user's email
 * @property username the user's username
 * @property password the user's password
 */
data class RegisterDTO(
    val email: String,
    val username: String,
    val password: String
)
