package pt.isel.pdm.battleships.services.users.models.getUsers

import pt.isel.pdm.battleships.services.utils.siren.SirenEntity

/**
 * The Get Users Output Model.
 *
 * @property totalCount the total number of users
 */
data class GetUsersOutputModel(
    val totalCount: Int
)

/**
 * The Get Users Output.
 */
typealias GetUsersOutput = SirenEntity<GetUsersOutputModel>
