package pt.isel.pdm.battleships.services.home.models

/**
 * A Version Control Repository.
 *
 * @property type the type of the repository, e.g. git
 * @property url the url of the repository
 */
data class VCRepositoryModel(
    val type: String,
    val url: String
)
