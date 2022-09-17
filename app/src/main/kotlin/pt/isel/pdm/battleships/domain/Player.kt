package pt.isel.pdm.battleships.domain

/**
 * Represents a player in the game.
 *
 * @property board the board of the player
 * @property score the points of the player
 *
 * @property isDefeated true if the player has no more ships, false otherwise
 */
data class Player(val board: Board, val score: Int = 0) {

    val isDefeated: Boolean
        get() = board.fleet.all { ship -> ship.isSunk }

    /**
     * Returns a new player with the updated score.
     *
     * @param points the points to add to the player's score
     * @return a new player with the updated score
     */
    fun updateScore(points: Int) = copy(score = score + points)
}
