package pt.isel.pdm.battleships.services.users

import pt.isel.pdm.battleships.services.exceptions.UnexpectedResponseException
import pt.isel.pdm.battleships.services.users.models.getUsers.GetUsersOutput
import pt.isel.pdm.battleships.services.users.models.getUsers.GetUsersUserModel
import pt.isel.pdm.battleships.services.users.models.login.LoginOutput
import pt.isel.pdm.battleships.services.users.models.register.RegisterOutput
import pt.isel.pdm.battleships.services.utils.APIResult
import pt.isel.pdm.battleships.services.utils.siren.EmbeddedSubEntity
import pt.isel.pdm.battleships.services.utils.siren.SirenEntity
import pt.isel.pdm.battleships.ui.utils.navigation.Rels
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
     * @return the API result with the user home Siren entity
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
     * @return the API result with the users DTO
     *
     * @throws UnexpectedResponseException if there is an unexpected response from the server
     * @throws IOException if there is an error while sending the request
     */
    suspend fun getUsers(): APIResult<GetUsersOutput> {
        val getUsersResult = usersService.getUsers(
            listUsersLink = links[Rels.LIST_USERS]
                ?: throw IllegalArgumentException("The list users link is missing")
        )

        if (getUsersResult !is APIResult.Success)
            return getUsersResult

        getUsersResult.data.entities?.filterIsInstance<EmbeddedSubEntity<GetUsersUserModel>>()
            ?.forEach { entity ->
                val username = entity.properties?.username
                    ?: throw IllegalArgumentException("The properties are missing")

                links["${Rels.USER}-$username"] = entity.links?.find {
                    Rels.SELF in it.rel
                }?.href?.path
                    ?: throw IllegalArgumentException("The user link is missing")
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
     * @return the API result with the authentication output DTO
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

        links[Rels.USER_HOME] = registerResult.data.links?.find {
            Rels.USER_HOME in it.rel
        }?.href?.path
            ?: throw IllegalStateException("The user home link is missing")

        return registerResult
    }

    /**
     * Logs in the user with the given [username] and [password].
     *
     * @param username the username of the user
     * @param password the password of the user
     *
     * @return the API result with the authentication output DTO
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

        links[Rels.USER_HOME] = loginResult.data.links?.find {
            Rels.USER_HOME in it.rel
        }?.href?.path
            ?: throw IllegalStateException("The user home link is missing")

        return loginResult
    }

    /**
     * Logs the user out.
     *
     * @throws UnexpectedResponseException if there is an unexpected response from the server
     * @throws IOException if there is an error while sending the request
     */
    suspend fun logout(): APIResult<SirenEntity<Unit>> {
        TODO("To be implemented")
    }
}
