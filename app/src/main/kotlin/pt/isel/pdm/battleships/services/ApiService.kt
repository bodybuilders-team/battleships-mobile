package pt.isel.pdm.battleships.services

/**
 * Represents the service that provides the game logic from the API.
 */
interface ApiService {

    // User

    // suspend fun getUser(username: String): User

    // suspend fun createUser(user: User): User

    suspend fun getUserRanking(username: String): RankingEntry

    // suspend fun registerUser(username: String, hashedPassword: String): User

    // suspend fun loginUser(username: String, hashedPassword: String): User

    // Ranking
    suspend fun getRanking(): List<RankingEntry>
}
