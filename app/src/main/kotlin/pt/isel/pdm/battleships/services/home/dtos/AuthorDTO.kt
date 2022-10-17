package pt.isel.pdm.battleships.services.home.dtos

/**
 * Represents an author of a game DTO.
 *
 * @property name the name of the author
 * @property email the email of the author
 * @property github the github of the author
 */
data class AuthorDTO(
    val name: String,
    val email: String,
    val github: String
)
