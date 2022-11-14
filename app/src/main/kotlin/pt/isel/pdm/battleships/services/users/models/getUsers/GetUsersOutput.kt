package pt.isel.pdm.battleships.services.users.models.getUsers

import pt.isel.pdm.battleships.services.utils.siren.SirenEntity

/**
 * The properties of a [GetUsersOutput]
 *
 * @property totalCount the total number of users
 */
data class GetUsersOutputModel(
    val totalCount: Int
)

/**
 * A Get Users DTO.
 */
typealias GetUsersOutput = SirenEntity<GetUsersOutputModel>
