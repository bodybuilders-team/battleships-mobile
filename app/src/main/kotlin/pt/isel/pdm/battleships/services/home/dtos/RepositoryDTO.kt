package pt.isel.pdm.battleships.services.home.dtos

/**
 * Represents a repository DTO.
 *
 * @property type the type of the repository
 * @property url the url of the repository
 */
data class RepositoryDTO(
    val type: String,
    val url: String
)
