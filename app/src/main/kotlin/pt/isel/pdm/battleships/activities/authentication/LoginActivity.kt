package pt.isel.pdm.battleships.activities.authentication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import pt.isel.pdm.battleships.DependenciesContainer
import pt.isel.pdm.battleships.activities.utils.getLinks
import pt.isel.pdm.battleships.activities.utils.viewModelInit
import pt.isel.pdm.battleships.ui.screens.authentication.login.LoginScreen
import pt.isel.pdm.battleships.viewModels.authentication.LoginViewModel

/**
 * Activity for the login screen.
 *
 * @property battleshipsService the service used to handle the battleships game
 * @property sessionManager the session manager used to handle the user session
 * @property viewModel the view model used to handle the login process
 */
class LoginActivity : ComponentActivity() {

    private val battleshipsService by lazy {
        (application as DependenciesContainer).battleshipsService
    }

    private val sessionManager by lazy {
        (application as DependenciesContainer).sessionManager
    }

    private val viewModel by viewModelInit {
        LoginViewModel(sessionManager, battleshipsService.usersService)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val links = intent.getLinks()

        val loginLink = links["login"] ?: throw IllegalStateException("Login link not found")

        setContent {
            LoginScreen(
                viewModel.state,
                onLogin = { username, password ->
                    viewModel.login(
                        loginLink = loginLink,
                        username = username,
                        password = password
                    )
                },
                onLoginSuccessful = {
                    finish()
                },
                errorMessage = viewModel.errorMessage,
                onBackButtonClicked = {
                    finish()
                }
            )
        }
    }
}
