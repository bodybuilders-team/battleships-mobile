package pt.isel.pdm.battleships.services.users.models.getUser

import pt.isel.pdm.battleships.services.utils.siren.SirenEntity

/**
 * The properties of a [GetUserOutput].
 *
 * @property username the username of the user
 * @property email the email of the user
 * @property points the points of the user
 */
data class GetUserOutputModel(
    val username: String,
    val email: String,
    val points: Int
)

/**
 * A User DTO.
 */
typealias GetUserOutput = SirenEntity<GetUserOutputModel>
