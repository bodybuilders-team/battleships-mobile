package pt.isel.pdm.battleships.domain.game

/**
 * Represents the phase of the game.
 */
enum class GamePhase {
    WAITING_FOR_PLAYERS,
    PLACING_SHIPS,
    IN_PROGRESS,
    FINISHED
}
