package pt.isel.pdm.battleships.service

/**
 * Represents the service that handles the battleships game.
 *
 * @property usersService The service used to handle the users.
 * @property gamesService The service used to handle the games.
 * @property playersService The service used to handle the players.
 */
class BattleshipsService(apiEndpoint: String) {

    val usersService: UserService = UserService(apiEndpoint)

    val gamesService: GameService = GameService(apiEndpoint)

    val playersService: PlayersService = PlayersService(apiEndpoint)
}
