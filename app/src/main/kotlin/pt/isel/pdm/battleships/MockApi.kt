package pt.isel.pdm.battleships

private class User(val username: String, val hashedPassword: String, val points: Int)

data class Player(val username: String, val points: Int)

data class RankedPlayer(
    val username: String,
    val points: Int,
    val position: Int
)

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
        User("yuri", "tarded", 42),
        User("il11", "gard", 22),
        User("ada", "asassa", 11),
        User("lulu", "dudu", 51),
        User("lala", "dada", 55),
        User("lili", "didi", 59),
        User("george", "shader", 91),
        User("james007", "bond", 7),
        User("patrick", "bateman", 14),
        User("jason1", "bourn", 21),
        User("jason22", "statham", 28),
        User("jason33", "momoa", 35),
        User("john41", "isaacs", 42),
        User("john12", "wick", 49),
        User("john22", "snow", 56),
        User("john31", "cena", 63),
        User("jordan51", "travolta", 70),
        User("jordan11", "peele", 77),
        User("jordan535", "belfort", 84),
        User("jordan123", "peterson", 91),
        User("kevin411", "klepper", 98),
        User("kevin221", "hart", 105),
        User("kevin001", "spacey", 112),
        User("kevin3221", "costner", 119),
        User("kevin33", "bacon", 126),
        User("kevin41", "mccallister", 133)
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
