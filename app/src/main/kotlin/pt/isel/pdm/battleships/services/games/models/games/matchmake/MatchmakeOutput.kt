package pt.isel.pdm.battleships.services.games.models.games.matchmake

import pt.isel.pdm.battleships.services.utils.siren.SirenEntity

/**
 * The Matchmake Output Model.
 *
 * @property wasCreated true if the game was created, false if it was joined
 */
data class MatchmakeOutputModel(
    val wasCreated: Boolean
)

/**
 * The Matchmake Output.
 */
typealias MatchmakeOutput = SirenEntity<MatchmakeOutputModel>
