package pt.isel.pdm.battleships.services.users.dtos

import pt.isel.pdm.battleships.services.utils.siren.SirenEntity

/**
 * Represents a DTO for a user.
 *
 * @property username the username of the user
 * @property email the email of the user
 * @property points the points of the user
 */
data class UserDTOProperties(
    val username: String,
    val email: String,
    val points: Int
)

typealias UserDTO = SirenEntity<UserDTOProperties>
