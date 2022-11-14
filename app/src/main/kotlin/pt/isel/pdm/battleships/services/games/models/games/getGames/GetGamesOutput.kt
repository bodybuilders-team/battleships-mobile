package pt.isel.pdm.battleships.services.games.models.games.getGames

import pt.isel.pdm.battleships.services.utils.siren.SirenEntity

/**
 * The properties of a Game DTO.
 *
 * @property totalCount the total number of games
 */
data class GetGamesOutputModel(
    val totalCount: Int
)

typealias GetGamesOutput = SirenEntity<GetGamesOutputModel>
