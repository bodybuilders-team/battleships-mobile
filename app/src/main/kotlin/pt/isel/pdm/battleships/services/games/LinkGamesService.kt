package pt.isel.pdm.battleships.services.games

import pt.isel.pdm.battleships.SessionManager
import pt.isel.pdm.battleships.services.exceptions.UnexpectedResponseException
import pt.isel.pdm.battleships.services.games.models.games.GameConfigModel
import pt.isel.pdm.battleships.services.games.models.games.getGame.GetGameOutput
import pt.isel.pdm.battleships.services.games.models.games.getGame.GetGameOutputModel
import pt.isel.pdm.battleships.services.games.models.games.getGameState.GetGameStateOutput
import pt.isel.pdm.battleships.services.games.models.games.getGames.GetGamesOutput
import pt.isel.pdm.battleships.services.games.models.games.joinGame.JoinGameOutput
import pt.isel.pdm.battleships.services.games.models.games.matchmake.MatchmakeOutput
import pt.isel.pdm.battleships.services.utils.APIResult
import pt.isel.pdm.battleships.services.utils.siren.EmbeddedLink
import pt.isel.pdm.battleships.services.utils.siren.EmbeddedSubEntity
import pt.isel.pdm.battleships.services.utils.siren.SirenEntity
import pt.isel.pdm.battleships.ui.utils.navigation.Rels
import java.io.IOException

/**
 * The service that handles the games, and keeps track of the links to the endpoints.
 *
 * @property sessionManager the session manager
 * @property links the links to the endpoints
 * @property gamesService the service that handles the games
 */
class LinkGamesService(
    private val sessionManager: SessionManager,
    private val links: MutableMap<String, String>,
    private val gamesService: GamesService
) {
    private val token
        get() = sessionManager.accessToken ?: throw IllegalStateException("No token available")

    /**
     * Gets all the games.
     *
     * @return the API result of the list games request
     *
     * @throws UnexpectedResponseException if there is an unexpected response from the server
     * @throws IOException if there is an error while sending the request
     */
    suspend fun getGames(): APIResult<GetGamesOutput> {
        val getGamesResult = gamesService.getGames(
            listGamesLink = links[Rels.LIST_GAMES]
                ?: throw IllegalStateException("The list games link is missing")
        )

        if (getGamesResult !is APIResult.Success)
            return getGamesResult

        getGamesResult.data.entities?.filterIsInstance<EmbeddedSubEntity<GetGameOutputModel>>()
            ?.forEach { entity ->
                val id = entity.properties?.id
                    ?: throw IllegalArgumentException("The properties are missing")

                links["${Rels.GAME}-$id"] = entity.links?.find {
                    Rels.SELF in it.rel
                }?.href?.path
                    ?: throw IllegalArgumentException("The game link is missing")

                links["${Rels.JOIN_GAME}-$id"] = entity.actions?.find {
                    it.name == Rels.JOIN_GAME
                }?.href?.path
                    ?: throw IllegalArgumentException("The join game link is missing")
            }

        return getGamesResult
    }

    /**
     * Creates a new game.
     *
     * @param gameConfig the game configuration
     *
     * @return the API result of the create game request
     *
     * @throws UnexpectedResponseException if there is an unexpected response from the server
     * @throws IOException if there is an error while sending the request
     */
    suspend fun createGame(gameConfig: GameConfigModel): APIResult<SirenEntity<Unit>> {
        val createGameResult = gamesService.createGame(
            token = token,
            createGameLink = links[Rels.CREATE_GAME]
                ?: throw IllegalStateException("The create game link is missing"),
            gameConfig = gameConfig
        )

        if (createGameResult !is APIResult.Success)
            return createGameResult

        val embeddedLinks = createGameResult.data.entities?.filterIsInstance<EmbeddedLink>()
            ?: throw IllegalStateException("The embedded links are missing")

        links[Rels.GAME] = embeddedLinks.find { Rels.GAME in it.rel }?.href?.path
            ?: throw IllegalStateException("The game link is missing")

        links[Rels.GAME_STATE] = embeddedLinks.find { Rels.GAME_STATE in it.rel }?.href?.path
            ?: throw IllegalStateException("The game state link is missing")

        return createGameResult
    }

    /**
     * Matchmakes a game with a specific configuration.
     *
     * @param gameConfig the DTO with the game's configuration
     *
     * @return the API result of the matchmake request
     *
     * @throws UnexpectedResponseException if there is an unexpected response from the server
     * @throws IOException if there is an error while sending the request
     */
    suspend fun matchmake(gameConfig: GameConfigModel): APIResult<MatchmakeOutput> {
        val matchmakeResult = gamesService.matchmake(
            token = token,
            matchmakeLink = links[Rels.MATCHMAKE]
                ?: throw IllegalStateException("The matchmake link is missing"),
            gameConfig = gameConfig
        )

        if (matchmakeResult !is APIResult.Success)
            return matchmakeResult

        val embeddedLinks = matchmakeResult.data.entities?.filterIsInstance<EmbeddedLink>()
            ?: throw IllegalStateException("The embedded links are missing")

        links[Rels.GAME] = embeddedLinks.find { Rels.GAME in it.rel }?.href?.path
            ?: throw IllegalStateException("The game link is missing")

        links[Rels.GAME_STATE] = embeddedLinks.find { Rels.GAME_STATE in it.rel }?.href?.path
            ?: throw IllegalStateException("The game state link is missing")

        return matchmakeResult
    }

    /**
     * Gets a game by id.
     *
     * @return the result of the get game operation
     *
     * @throws UnexpectedResponseException if there is an unexpected response from the server
     * @throws IOException if there is an error while sending the request
     */
    suspend fun getGame(): APIResult<GetGameOutput> {
        val getGameResult = gamesService.getGame(
            token = token,
            gameLink = links[Rels.GAME]
                ?: throw IllegalStateException("The game link is missing")
        )

        if (getGameResult !is APIResult.Success)
            return getGameResult

        val embeddedLinks = getGameResult.data.entities?.filterIsInstance<EmbeddedLink>()
            ?: throw IllegalStateException("The embedded links are missing")

        links[Rels.GAME_STATE] = embeddedLinks.find { Rels.GAME_STATE in it.rel }?.href?.path
            ?: throw IllegalStateException("The game state link is missing")

        links.putAll(getGameResult.data.getActionLinks())

        return getGameResult
    }

    /**
     * Gets the state of a game.
     *
     * @return the API result of the get game state request
     *
     * @throws UnexpectedResponseException if there is an unexpected response from the server
     * @throws IOException if there is an error while sending the request
     */
    suspend fun getGameState(): APIResult<GetGameStateOutput> =
        gamesService.getGameState(
            token = token,
            gameStateLink = links[Rels.GAME_STATE]
                ?: throw IllegalStateException("The game state link is missing")
        )

    /**
     * Joins a game.
     *
     * @return the API result of the join game request
     *
     * @throws UnexpectedResponseException if there is an unexpected response from the server
     * @throws IOException if there is an error while sending the request
     */
    suspend fun joinGame(): APIResult<JoinGameOutput> {
        val joinGameResult = gamesService.joinGame(
            token = token,
            joinGameLink = links[Rels.JOIN_GAME]
                ?: throw IllegalStateException("The join game link is missing")
        )

        if (joinGameResult !is APIResult.Success)
            return joinGameResult

        val embeddedLinks = joinGameResult.data.entities?.filterIsInstance<EmbeddedLink>()
            ?: throw IllegalStateException("The embedded links are missing")

        links[Rels.GAME] = embeddedLinks.find { Rels.GAME in it.rel }?.href?.path
            ?: throw IllegalStateException("The game link is missing")

        links[Rels.GAME_STATE] = embeddedLinks.find { Rels.GAME_STATE in it.rel }?.href?.path
            ?: throw IllegalStateException("The game state link is missing")

        return joinGameResult
    }

    /**
     * Leaves a game.
     *
     * @param gameLink the link to the game
     *
     * @return the API result of the leave game request
     *
     * @throws UnexpectedResponseException if there is an unexpected response from the server
     * @throws IOException if there is an error while sending the request
     */
    suspend fun leaveGame(gameLink: String) {
        // TODO: To be implemented
    }
}
