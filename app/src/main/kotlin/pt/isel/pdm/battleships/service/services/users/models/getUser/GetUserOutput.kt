package pt.isel.pdm.battleships.service.services.users.models.getUser

import pt.isel.pdm.battleships.service.media.siren.SirenEntity

/**
 * The Get User Output Model.
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
 * The Get User Output.
 */
typealias GetUserOutput = SirenEntity<GetUserOutputModel>
