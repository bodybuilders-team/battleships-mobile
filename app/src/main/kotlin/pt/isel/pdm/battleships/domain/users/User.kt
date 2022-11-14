package pt.isel.pdm.battleships.domain.users

/**
 * A user of the application.
 *
 * @param username the user's username
 * @param email the user's email
 * @param points the user's points
 */
data class User(
    val username: String,
    val email: String,
    val points: Int
) {

    /**
     * Converts this user to a ranked user.
     *
     * @param rank the user's rank
     */
    fun toRankedUser(rank: Int) = RankedUser(
        username = username,
        points = points,
        rank = rank
    )
}
