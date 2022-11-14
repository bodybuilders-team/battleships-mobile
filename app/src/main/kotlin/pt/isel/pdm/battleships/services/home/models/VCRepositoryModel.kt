package pt.isel.pdm.battleships.services.home.models

/**
 * The VC Repository Model.
 *
 * @property type the type of the repository, e.g. git
 * @property url the url of the repository
 */
data class VCRepositoryModel(
    val type: String,
    val url: String
)
