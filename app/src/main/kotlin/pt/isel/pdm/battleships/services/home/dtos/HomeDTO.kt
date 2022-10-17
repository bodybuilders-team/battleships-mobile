package pt.isel.pdm.battleships.services.home.dtos

import pt.isel.pdm.battleships.services.utils.siren.SirenEntity

/**
 * Represents a home DTO.
 *
 * @property title the title of the application
 * @property version the version of the application
 * @property description the description of the application
 * @property authors the authors of the application
 * @property repository the repository of the application
 */
data class HomeDTOProperties(
    val title: String,
    val version: String,
    val description: String,
    val authors: List<AuthorDTO>,
    val repository: RepositoryDTO
)

typealias HomeDTO = SirenEntity<HomeDTOProperties>
