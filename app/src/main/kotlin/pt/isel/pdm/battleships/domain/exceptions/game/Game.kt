package pt.isel.pdm.battleships.domain.exceptions.game

/**
 * Represents a battleship game.
 *
 * @property player1 the first player
 * @property player2 the second player
 *
 * @property isOver true if the game is over, false otherwise
 */
data class Game(val player1: Player, val player2: Player) {

    val isOver: Boolean
        get() = player1.isDefeated || player2.isDefeated
}
