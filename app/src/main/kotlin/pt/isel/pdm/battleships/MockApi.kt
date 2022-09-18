package pt.isel.pdm.battleships

private class User(val username: String, val hashedPassword: String, val points: Int)

data class Player(val username: String, val points: Int)

object MockApi {
    private val users = mutableListOf<User>(
        User("joe", "mama", 69),
        User("mike", "ock", 420),
        User("yuri", "tarded", 42)
    )

    fun getPlayers(): List<Player> =
        users.map { Player(it.username, it.points) }

    fun register(username: String, hashedPassword: String): String {
        if (users.find { player -> player.username == username } != null) {
            return "USERNAME_EXISTS"
        }

        users.add(User(username, hashedPassword, 0))

        return "SUCCESSFUL"
    }

    fun login(username: String, hashedPassword: String): String {
        val player = users.find { player -> player.username == username }
            ?: return "NO_USERNAME"

        return if (player.hashedPassword == hashedPassword) {
            "SUCCESSFUL"
        } else "WRONG_PASSWORD"
    }
}
