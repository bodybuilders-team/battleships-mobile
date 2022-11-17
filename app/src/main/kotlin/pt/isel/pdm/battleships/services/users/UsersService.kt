package pt.isel.pdm.battleships.services.users

import com.google.gson.Gson
import okhttp3.OkHttpClient
import pt.isel.pdm.battleships.services.HTTPService
import pt.isel.pdm.battleships.services.exceptions.UnexpectedResponseException
import pt.isel.pdm.battleships.services.users.models.getUsers.GetUsersOutput
import pt.isel.pdm.battleships.services.users.models.getUsers.GetUsersOutputModel
import pt.isel.pdm.battleships.services.users.models.getUsers.GetUsersUserModel
import pt.isel.pdm.battleships.services.users.models.login.LoginInput
import pt.isel.pdm.battleships.services.users.models.login.LoginOutput
import pt.isel.pdm.battleships.services.users.models.logout.LogoutInput
import pt.isel.pdm.battleships.services.users.models.register.RegisterInput
import pt.isel.pdm.battleships.services.users.models.register.RegisterOutput
import pt.isel.pdm.battleships.services.utils.APIResult
import pt.isel.pdm.battleships.services.utils.siren.EmbeddedSubEntity
import pt.isel.pdm.battleships.services.utils.siren.SirenEntity
import java.io.IOException

/**
 * The service that handles the battleships game.
 *
 * @property apiEndpoint the API endpoint
 * @property httpClient the HTTP client
 * @property jsonEncoder the JSON encoder used to serialize/deserialize objects
 */
class UsersService(
    apiEndpoint: String,
    httpClient: OkHttpClient,
    jsonEncoder: Gson
) : HTTPService(apiEndpoint, httpClient, jsonEncoder) {

    /**
     * Gets the user home.
     *
     * @param userHomeLink the link to the user home endpoint
     *
     * @return the API result of the get user home request
     *
     * @throws UnexpectedResponseException if there is an unexpected response from the server
     * @throws IOException if there is an error while sending the request
     */
    suspend fun getUserHome(userHomeLink: String): APIResult<SirenEntity<Unit>> =
        get(link = userHomeLink)

    /**
     * Gets all the users.
     *
     * @param listUsersLink the link to the list users endpoint
     *
     * @return the API result of the get users request
     *
     * @throws UnexpectedResponseException if there is an unexpected response from the server
     * @throws IOException if there is an error while sending the request
     */
    suspend fun getUsers(listUsersLink: String): APIResult<GetUsersOutput> {
        val getUserResult = get<GetUsersOutputModel>(link = listUsersLink)

        if (getUserResult !is APIResult.Success)
            return getUserResult

        return APIResult.Success(
            data = getUserResult.data.copy(
                entities = getUserResult.data.entities
                    ?.filterIsInstance<EmbeddedSubEntity<*>>()
                    ?.map { entity -> entity.getEmbeddedSubEntity<GetUsersUserModel>() }
            )
        )
    }

    /**
     * Registers the user with the given [email], [username] and [password].
     *
     * @param registerLink the link to the register endpoint
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
        registerLink: String,
        email: String,
        username: String,
        password: String
    ): APIResult<RegisterOutput> =
        post(
            link = registerLink,
            body = RegisterInput(username = username, email = email, password = password)
        )

    /**
     * Logs in the user with the given [username] and [password].
     *
     * @param loginLink the link to the login endpoint
     * @param username the username of the user
     * @param password the password of the user
     *
     * @return the API result of the login request
     *
     * @throws UnexpectedResponseException if there is an unexpected response from the server
     * @throws IOException if there is an error while sending the request
     */
    suspend fun login(
        loginLink: String,
        username: String,
        password: String
    ): APIResult<LoginOutput> =
        post(
            link = loginLink,
            body = LoginInput(username, password)
        )

    /**
     * Logs the user out.
     *
     * @param logoutLink the link to the logout endpoint
     * @param refreshToken the refresh token of the user
     *
     * @return the API result of the logout request
     *
     * @throws UnexpectedResponseException if there is an unexpected response from the server
     * @throws IOException if there is an error while sending the request
     */
    suspend fun logout(
        logoutLink: String,
        refreshToken: String
    ): APIResult<SirenEntity<Unit>> =
        post(
            link = logoutLink,
            body = LogoutInput(refreshToken)
        )
}
