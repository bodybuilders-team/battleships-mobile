package pt.isel.pdm.battleships.services.games.dtos

import pt.isel.pdm.battleships.services.utils.siren.SirenEntity

/**
 * Represents the properties of a Matchmake DTO.
 *
 * @property wasCreated true if the game was created, false if it was joined
 */
data class MatchmakeDTOProperties(
    val wasCreated: Boolean
)

typealias MatchmakeDTO = SirenEntity<MatchmakeDTOProperties>
