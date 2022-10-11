package pt.isel.pdm.battleships.services.games.results

import pt.isel.pdm.battleships.services.dtos.ErrorDTO
import pt.isel.pdm.battleships.services.games.dtos.MatchmakeDTO

/**
 * The result of a matchmake attempt.
 */
sealed class MatchmakeResult {

    /**
     * The matchmake was successful.
     *
     * @property dto the matchmake DTO
     */
    class Success(val dto: MatchmakeDTO) : MatchmakeResult()

    /**
     * The matchmake failed.
     *
     * @property error the error DTO
     */
    class Failure(val error: ErrorDTO) : MatchmakeResult()
}
