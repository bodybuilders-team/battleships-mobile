package pt.isel.pdm.battleships.domain.games

import pt.isel.pdm.battleships.domain.games.board.MyBoard
import pt.isel.pdm.battleships.domain.games.ship.Ship

/**
 * A player in the game.
 *
 * @property board the board of the player
 * @property score the points of the player
 *
 * @property isDefeated true if the player has no more ships, false otherwise
 */
data class Player(val board: MyBoard, val score: Int = 0) {

    val isDefeated: Boolean
        get() = board.fleet.all(Ship::isSunk)

    /**
     * Returns a new player with the updated score.
     *
     * @param points the points to add to the player's score
     * @return a new player with the updated score
     */
    fun updateScore(points: Int) = copy(score = score + points)
}
