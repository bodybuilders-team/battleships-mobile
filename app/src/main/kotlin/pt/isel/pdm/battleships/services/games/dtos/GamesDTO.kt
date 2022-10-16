package pt.isel.pdm.battleships.services.games.dtos

import pt.isel.pdm.battleships.services.utils.siren.SirenEntity

/**
 * Represents the properties of a Game DTO.
 *
 * @property totalCount the total number of games
 */
data class GamesDTOProperties(
    val totalCount: Int
)

typealias GamesDTO = SirenEntity<GamesDTOProperties>
