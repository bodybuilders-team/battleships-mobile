package pt.isel.pdm.battleships.service.services.home.models

/**
 * The Author Model.
 *
 * @property name the name of the author
 * @property email the email of the author
 * @property github the github of the author
 */
data class AuthorModel(
    val name: String,
    val email: String,
    val github: String
)
