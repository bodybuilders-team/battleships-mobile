package pt.isel.pdm.battleships.domain.games.game

/**
 * The cause of the end of the game.
 *
 * @property YOU you won
 * @property OPPONENT the opponent won
 */
enum class WinningPlayer {
    YOU,
    OPPONENT,
    NONE
}
