package pt.isel.pdm.battleships.domain.games.game

/**
 * The cause of the end of the game.
 *
 * @property DESTRUCTION the game ended because a player's fleet was destroyed
 * @property RESIGNATION the game ended because a player resigned
 * @property TIMEOUT the game ended because a player took too long
 */
enum class EndGameCause {
    DESTRUCTION,
    RESIGNATION,
    TIMEOUT;

    companion object {
        operator fun invoke(phase: String) = valueOf(phase)
    }
}
