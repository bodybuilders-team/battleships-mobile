package pt.isel.pdm.battleships.domain.users

/**
 * A ranked user.
 * Contains the user's rank.
 *
 * @param username the user's username
 * @param points the user's points
 * @param rank the user's rank
 */ // TODO: Change to have rankByPoints and rankByWins maybe?
data class RankedUser(
    val username: String,
    val points: Int,
    val rank: Int
)
