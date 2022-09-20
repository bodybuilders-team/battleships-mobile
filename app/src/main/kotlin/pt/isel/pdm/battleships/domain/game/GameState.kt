package pt.isel.pdm.battleships.domain.game

/**
 * Represents the state of the game.
 */
enum class GameState {
    WAITING_FOR_PLAYERS,
    PLACING_SHIPS,
    IN_PROGRESS,
    FINISHED
}
