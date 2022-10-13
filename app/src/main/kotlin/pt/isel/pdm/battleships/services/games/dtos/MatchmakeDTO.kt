package pt.isel.pdm.battleships.services.games.dtos

/**
 * Represents a Matchmake DTO.
 *
 * @property gameId the id of the game
 * @property wasCreated true if the game was created, false if it was joined
 */
data class MatchmakeDTO(
    val wasCreated: Boolean,
    val game: GameDTO
)
