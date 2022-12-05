package pt.isel.pdm.battleships.service.services.users

import pt.isel.pdm.battleships.service.connection.APIResult
import pt.isel.pdm.battleships.service.connection.UnexpectedResponseException
import pt.isel.pdm.battleships.service.media.siren.SirenEntity
import pt.isel.pdm.battleships.service.services.users.models.getUsers.GetUsersOutput
import pt.isel.pdm.battleships.service.services.users.models.getUsers.GetUsersUserModel
import pt.isel.pdm.battleships.service.services.users.models.login.LoginOutput
import pt.isel.pdm.battleships.service.services.users.models.register.RegisterOutput
import pt.isel.pdm.battleships.ui.screens.shared.navigation.Rels
import java.io.IOException

/**
 * The service that handles the users, and keeps track of the links to the endpoints.
 *
 * @property links the links to the endpoints
 * @property usersService the service that handles the users
 */
class LinkUsersService(
    private val links: MutableMap<String, String>,
    private val usersService: UsersService
) {

    /**
     * Gets the user home.
     *
     * @return the API result of the get user home request
     *
     * @throws UnexpectedResponseException if there is an unexpected response from the server
     * @throws IOException if there is an error while sending the request
     */
    suspend fun getUserHome(): APIResult<SirenEntity<Unit>> {
        val getUserHomeResult = try {
            usersService.getUserHome(
                userHomeLink = links[Rels.USER_HOME]
                    ?: throw IllegalStateException("The user home link is missing")
            )
        } catch (e: java.lang.Exception) {
            throw e
        }

        if (getUserHomeResult is APIResult.Success)
            links.putAll(getUserHomeResult.data.getActionLinks())

        return getUserHomeResult
    }

    /**
     * Gets all the users.
     *
     * @param sortParam the parameter used to sort the users
     * @param sortValue the value used to sort the users by the [sortParam]
     *
     * @return the API result of the get users request
     *
     * @throws UnexpectedResponseException if there is an unexpected response from the server
     * @throws IOException if there is an error while sending the request
     */
    suspend fun getUsers(sortParam: String, sortValue: String): APIResult<GetUsersOutput> {
        val getUsersResult = usersService.getUsers(
            listUsersLink = links[Rels.LIST_USERS]?.let { "$it?$sortParam=$sortValue" }
                ?: throw IllegalArgumentException("The list users link is missing")
        )

        if (getUsersResult !is APIResult.Success)
            return getUsersResult

        getUsersResult.data.embeddedSubEntities<GetUsersUserModel>(Rels.ITEM, Rels.USER)
            .forEach { entity ->
                val username = entity.properties?.username
                    ?: throw IllegalArgumentException("The properties are missing")

                links["${Rels.USER}-$username"] = entity.getLink(Rels.SELF).href.path
            }

        return getUsersResult
    }

    /**
     * Registers the user with the given [email], [username] and [password].
     *
     * @param email the email of the user
     * @param username the username of the user
     * @param password the password of the user
     *
     * @return the API result of the register request
     *
     * @throws UnexpectedResponseException if there is an unexpected response from the server
     * @throws IOException if there is an error while sending the request
     */
    suspend fun register(
        email: String,
        username: String,
        password: String
    ): APIResult<RegisterOutput> {
        val registerResult = usersService.register(
            registerLink = links[Rels.REGISTER]
                ?: throw IllegalStateException("The register link is missing"),
            email = email,
            username = username,
            password = password
        )

        if (registerResult !is APIResult.Success)
            return registerResult

        links[Rels.USER_HOME] = registerResult.data.getLink(Rels.USER_HOME).href.path

        return registerResult
    }

    /**
     * Logs in the user with the given [username] and [password].
     *
     * @param username the username of the user
     * @param password the password of the user
     *
     * @return the API result of the login request
     *
     * @throws UnexpectedResponseException if there is an unexpected response from the server
     * @throws IOException if there is an error while sending the request
     */
    suspend fun login(
        username: String,
        password: String
    ): APIResult<LoginOutput> {
        val loginResult = usersService.login(
            loginLink = links[Rels.LOGIN]
                ?: throw IllegalArgumentException("The login link is missing"),
            username = username,
            password = password
        )

        if (loginResult !is APIResult.Success)
            return loginResult

        links[Rels.USER_HOME] = loginResult.data.getLink(Rels.USER_HOME).href.path

        return loginResult
    }

    /**
     * Logs the user out.
     *
     * @param refreshToken the refresh token of the user
     *
     * @return the API result of the logout request
     *
     * @throws UnexpectedResponseException if there is an unexpected response from the server
     * @throws IOException if there is an error while sending the request
     */
    suspend fun logout(refreshToken: String): APIResult<SirenEntity<Unit>> =
        usersService.logout(
            logoutLink = links[Rels.LOGOUT]
                ?: throw IllegalArgumentException("The logout link is missing"),
            refreshToken = refreshToken
        )
}
