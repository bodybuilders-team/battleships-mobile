package pt.isel.pdm.battleships.services.games.dtos

/**
 * Represents a Game State DTO.
 *
 * @property phase the phase of the game
 * @property phaseEndTime the time when the current phase ends
 * @property round the round of the game
 * @property turn the turn of the game
 * @property winner the winner of the game
 */
data class GameStateDTO(
    val phase: String,
    val phaseEndTime: Long,
    val round: Int?,
    val turn: String?,
    val winner: String?
)
