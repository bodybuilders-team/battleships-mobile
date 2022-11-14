package pt.isel.pdm.battleships.services.games.models.games.matchmake

import pt.isel.pdm.battleships.services.utils.siren.SirenEntity

/**
 * The properties of a Matchmake DTO.
 *
 * @property wasCreated true if the game was created, false if it was joined
 */
data class MatchmakeOutputModel(
    val wasCreated: Boolean
)

typealias MatchmakeOutput = SirenEntity<MatchmakeOutputModel>
