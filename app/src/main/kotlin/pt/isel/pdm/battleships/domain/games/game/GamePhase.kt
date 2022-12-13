package pt.isel.pdm.battleships.domain.games.game

/**
 * The game phases.
 *
 * @property WAITING_FOR_PLAYERS the game is waiting for players to join
 * @property DEPLOYING_FLEETS the game is waiting for players to place their ships
 * @property IN_PROGRESS the game is in progress
 * @property FINISHED the game is finished
 */
enum class GamePhase {
    WAITING_FOR_PLAYERS,
    DEPLOYING_FLEETS,
    IN_PROGRESS,
    FINISHED;

    companion object {
        operator fun invoke(phase: String) = valueOf(phase)
    }
}
