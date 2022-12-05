package pt.isel.pdm.battleships.service.services.games.models.games.getGames

import pt.isel.pdm.battleships.service.media.siren.SirenEntity

/**
 * The Get Games Output Model.
 *
 * @property totalCount the total number of games
 */
data class GetGamesOutputModel(
    val totalCount: Int
)

/**
 * The Get Games Output.
 */
typealias GetGamesOutput = SirenEntity<GetGamesOutputModel>
