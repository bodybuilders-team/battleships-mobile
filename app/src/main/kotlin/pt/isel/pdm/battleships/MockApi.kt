package pt.isel.pdm.battleships

private class User(val username: String, val hashedPassword: String, val points: Int)

data class Player(val username: String, val points: Int)

enum class RegisterStatus {
    USERNAME_EXISTS,
    SUCCESSFUL
}

enum class LoginStatus {
    USERNAME_NOT_FOUND,
    WRONG_PASSWORD,
    SUCCESSFUL
}

object MockApi {
    private val users = mutableListOf(
        User("joe", "mama", 69),
        User("mike", "ock", 420),
        User("yuri", "tarded", 42)
    )

    fun getPlayers(): List<Player> =
        users.map { Player(it.username, it.points) }

    fun register(username: String, hashedPassword: String): RegisterStatus {
        if (users.find { player -> player.username == username } != null) {
            return RegisterStatus.USERNAME_EXISTS
        }

        users.add(User(username, hashedPassword, 0))

        return RegisterStatus.SUCCESSFUL
    }

    fun login(username: String, hashedPassword: String): LoginStatus {
        val player = users.find { player -> player.username == username }
            ?: return LoginStatus.USERNAME_NOT_FOUND

        return if (player.hashedPassword == hashedPassword) {
            LoginStatus.SUCCESSFUL
        } else LoginStatus.WRONG_PASSWORD
    }
}
