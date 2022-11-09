package pt.isel.pdm.battleships.services.users.dtos

import pt.isel.pdm.battleships.services.utils.siren.SirenEntity

/**
 * Represents the properties of a User DTO.
 *
 * @property totalCount the total number of users
 */
data class UsersDTOProperties(
    val totalCount: Int
)

typealias UsersDTO = SirenEntity<UsersDTOProperties>
