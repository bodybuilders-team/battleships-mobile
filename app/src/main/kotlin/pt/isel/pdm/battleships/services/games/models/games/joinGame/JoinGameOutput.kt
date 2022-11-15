package pt.isel.pdm.battleships.services.games.models.games.joinGame

import pt.isel.pdm.battleships.services.utils.siren.SirenEntity

/**
 * The Join Game Output Model.
 *
 * @property gameId the id of the joined game
 */
data class JoinGameOutputModel(
    val gameId: Int
)

/**
 * The Join Game Output.
 */
typealias JoinGameOutput = SirenEntity<JoinGameOutputModel>
