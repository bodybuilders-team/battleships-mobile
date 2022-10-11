package pt.isel.pdm.battleships.services.games.results

import pt.isel.pdm.battleships.services.dtos.ErrorDTO
import pt.isel.pdm.battleships.services.games.dtos.GameDTO

/**
 * The result of a game creation attempt.
 */
sealed class GameResult {

    /**
     * The game was successfully created.
     *
     * @property game the game DTO
     */
    class Success(val game: GameDTO) : GameResult()

    /**
     * The game creation failed.
     *
     * @property error the error DTO
     */
    class Failure(val error: ErrorDTO) : GameResult()
}
