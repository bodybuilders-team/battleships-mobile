package pt.isel.pdm.battleships.domain.games.game

/**
 * Represents the phase of the game.
 */
enum class GamePhase {
    WAITING_FOR_PLAYERS,
    PLACING_SHIPS,
    IN_PROGRESS,
    FINISHED
}
