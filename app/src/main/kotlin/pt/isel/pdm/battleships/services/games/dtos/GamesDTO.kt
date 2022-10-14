package pt.isel.pdm.battleships.services.games.dtos

import pt.isel.pdm.battleships.services.utils.siren.SirenEntity

/**
 * Represents the response body of a game list request.
 *
 * @property totalCount the total number of games
 */
data class GamesDTOProperties(
    val totalCount: Int
)

typealias GamesDTO = SirenEntity<GamesDTOProperties>

val gamesDTOType = SirenEntity.getType<GamesDTOProperties>()
