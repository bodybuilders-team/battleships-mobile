package pt.isel.pdm.battleships.services

/**
 * Represents a ranking entry.
 *
 * @property position the position of the player in the ranking
 * @property username the name of the player
 * @property points the number of points of the player
 */
data class RankingEntry(
    val position: Int,
    val username: String,
    val points: Int
)
