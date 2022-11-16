package pt.isel.pdm.battleships.domain.users

/**
 * A ranked user.
 * Contains the user's rank.
 *
 * @property username the user's username
 * @property points the user's points
 * @property numberOfGamesPlayed the number of games played by the user
 * @property rankByPoints the user's rank by points
 */
data class RankedUser(
    val username: String,
    val points: Int,
    val numberOfGamesPlayed: Int,
    val rankByPoints: Int
) {
    constructor(user: User, rankByPoints: Int) : this(
        username = user.username,
        points = user.points,
        numberOfGamesPlayed = user.numberOfGamesPlayed,
        rankByPoints = rankByPoints
    )
}

/**
 * Converts a list of [User]s to a list of [RankedUser]s.
 *
 * @receiver the list of [User]s
 * @return the list of [RankedUser]s
 */
fun List<User>.toRankedUsers(): List<RankedUser> =
    mapIndexed { index, user ->
        RankedUser(
            user = user,
            rankByPoints = index + 1
        )
    }
