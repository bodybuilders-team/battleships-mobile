package pt.isel.pdm.battleships.domain.users

data class User(
    val username: String,
    val email: String,
    val points: Int
) {

    fun toRankedUser(rank: Int) = RankedUser(username, points, rank)
}

data class RankedUser(
    val username: String,
    val points: Int,
    val rank: Int
)
