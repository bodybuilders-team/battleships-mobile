package pt.isel.pdm.battleships.activities.authentication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import pt.isel.pdm.battleships.DependenciesContainer
import pt.isel.pdm.battleships.activities.utils.getLinks
import pt.isel.pdm.battleships.activities.utils.viewModelInit
import pt.isel.pdm.battleships.ui.screens.authentication.register.RegisterScreen
import pt.isel.pdm.battleships.viewModels.authentication.RegisterViewModel

/**
 * Activity for the register screen.
 *
 * @property battleshipsService the service used to handle the battleships game
 * @property sessionManager the session manager used to handle the user session
 * @property viewModel the view model used to handle the login process
 */
class RegisterActivity : ComponentActivity() {

    private val battleshipsService by lazy {
        (application as DependenciesContainer).battleshipsService
    }

    private val sessionManager by lazy {
        (application as DependenciesContainer).sessionManager
    }

    private val viewModel: RegisterViewModel by viewModelInit {
        RegisterViewModel(sessionManager, battleshipsService.usersService)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val links = intent.getLinks()

        val registerLink = links["register"] ?: throw IllegalStateException("Login link not found")

        setContent {
            RegisterScreen(
                state = viewModel.state,
                onRegister = { email, username, password ->
                    viewModel.register(
                        registerLink = registerLink,
                        email = email,
                        username = username,
                        password = password
                    )
                },
                onRegisterSuccessful = {
                    finish()
                },
                errorMessage = viewModel.errorMessage,
                onBackButtonClicked = { finish() }
            )
        }
    }
}
