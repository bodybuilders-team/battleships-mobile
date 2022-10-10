package pt.isel.pdm.battleships.service

/**
 * Represents the service that handles the battleships game.
 *
 * @property usersService The service used to handle the users.
 * @property gamesService The service used to handle the games.
 * @property playersService The service used to handle the players.
 */
class BattleshipsService {

    val usersService: UserService = UserService()

    val gamesService: GameService = GameService()

    val playersService: PlayersService = PlayersService()

    companion object {
        const val API_URI = "http://localhost:8888"
    }
}
