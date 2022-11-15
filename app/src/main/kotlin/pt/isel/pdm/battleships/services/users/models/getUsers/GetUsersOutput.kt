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

/*
TODO Study the need of specific siren entities (to resolve sub entities not having type known)

data class GetUsersSirenEntity<T>(
    val `class`: List<String>? = null,
    val properties: T? = null,
    val entities: List<SubEntity>? = null,
    val actions: List<Action>? = null,
    val links: List<Link>? = null,
    val title: String? = null
) {
    init {

    }
}*/
