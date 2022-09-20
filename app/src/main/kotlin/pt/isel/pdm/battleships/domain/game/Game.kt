package pt.isel.pdm.battleships.domain.game

/**
 * Represents a battleship game.
 *
 * @property player1 the first player
 * @property player2 the second player
 * @property shotsPerRound the number of shots per round
 * @property maxTimeForLayoutPhase the maximum time allowed for the layout phase
 * @property maxTimeForShootingPhase the maximum time allowed for the shooting phase
 *
 * @property isOver true if the game is over, false otherwise
 */
data class Game(
    val player1: Player,
    val player2: Player,
    val shotsPerRound: Int,
    val maxTimeForLayoutPhase: Long,
    val maxTimeForShootingPhase: Long
) {

    val isOver: Boolean
        get() = player1.isDefeated || player2.isDefeated
}
