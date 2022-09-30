package pt.isel.pdm.battleships.domain.game

/**
 * Represents a game configuration.
 */
data class GameConfig(
    val boardSize: Int,
    val shotsPerTurn: Int,
    val timePerTurn: Int,
    val timeForBoardConfig: Int
)
