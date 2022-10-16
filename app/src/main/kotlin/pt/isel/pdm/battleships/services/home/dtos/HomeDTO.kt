package pt.isel.pdm.battleships.services.home.dtos

import pt.isel.pdm.battleships.services.utils.siren.SirenEntity

data class HomeDTOProperties(
    val title: String,
    val version: String,
    val description: String,
    val authors: List<AuthorDTO>,
    val repository: RepositoryDTO
)

typealias HomeDTO = SirenEntity<HomeDTOProperties>
