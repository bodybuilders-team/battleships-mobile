package pt.isel.pdm.battleships.services.users

import pt.isel.pdm.battleships.services.UnexpectedResponseException
import pt.isel.pdm.battleships.services.users.dtos.AuthenticationOutputDTO
import pt.isel.pdm.battleships.services.users.dtos.UsersDTO
import pt.isel.pdm.battleships.services.utils.APIResult
import pt.isel.pdm.battleships.services.utils.siren.SirenEntity
import pt.isel.pdm.battleships.ui.utils.navigation.Rels
import java.io.IOException

/**
 * TODO Comment
 */
class LinkUsersService(
    private val links: MutableMap<String, String>,
    private val usersService: UsersService
) {

    /**
     * Registers the user with the given [email], [username] and [password].
     *
     * @param email the email of the user
     * @param username the username of the user
     * @param password the password of the user
     *
     * @return the authentication result
     * @throws UnexpectedResponseException if there is an unexpected response from the server
     * @throws IOException if there is an error while sending the request
     */
    suspend fun register(
        email: String,
        username: String,
        password: String
    ): APIResult<AuthenticationOutputDTO> =
        usersService.register(
            registerLink = links[Rels.REGISTER]
                ?: throw IllegalArgumentException("The register link is missing"),
            email = email,
            username = username,
            password = password
        )

    /**
     * Logs in the user with the given [username] and [password].
     *
     * @param username the username of the user
     * @param password the password of the user
     *
     * @return the authentication result
     * @throws UnexpectedResponseException if there is an unexpected response from the server
     * @throws IOException if there is an error while sending the request
     */
    suspend fun login(
        username: String,
        password: String
    ): APIResult<AuthenticationOutputDTO> =
        usersService.login(
            loginLink = links[Rels.LOGIN]
                ?: throw IllegalArgumentException("The login link is missing"),
            username = username,
            password = password
        )

    /**
     * Gets all the users.
     *
     * @throws UnexpectedResponseException if there is an unexpected response from the server
     * @throws IOException if there is an error while sending the request
     */
    suspend fun getUsers(): APIResult<UsersDTO> =
        usersService.getUsers(
            listUsersLink = links[Rels.LIST_USERS]
                ?: throw IllegalArgumentException("The list users link is missing")
        )

    /**
     * Gets the user home.
     *
     * @return the user home
     */
    suspend fun getUserHome(): APIResult<SirenEntity<Unit>> =
        usersService.getUserHome(
            userHomeLink = links[Rels.USER_HOME]
                ?: throw IllegalArgumentException("The user home link is missing")
        )
}
