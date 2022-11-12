package pt.isel.pdm.battleships.ui.screens.authentication.register

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import pt.isel.pdm.battleships.DependenciesContainer
import pt.isel.pdm.battleships.ui.screens.authentication.AuthenticationViewModel
import pt.isel.pdm.battleships.ui.utils.showToast
import pt.isel.pdm.battleships.utils.Links
import pt.isel.pdm.battleships.utils.Links.Companion.getLinks
import pt.isel.pdm.battleships.utils.Rels.REGISTER
import pt.isel.pdm.battleships.utils.viewModelInit

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
        RegisterViewModel(
            usersService = battleshipsService.usersService,
            sessionManager = sessionManager
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val links = intent.getLinks()

        val registerLink = links[REGISTER]
            ?: throw IllegalStateException("$REGISTER link not found")

        lifecycleScope.launch {
            viewModel.events.collect {
                handleEvent(it)
            }
        }

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
                    val resultIntent = Intent()
                    val userHomeLink =
                        viewModel.link ?: throw IllegalStateException("Link not found")
                    resultIntent.putExtra(
                        Links.LINKS_KEY,
                        Links(mapOf("user-home" to userHomeLink))
                    )

                    setResult(RESULT_OK, resultIntent)
                    finish()
                },
                onBackButtonClicked = {
                    setResult(RESULT_CANCELED, null)
                    finish()
                }
            )
        }
    }

    private suspend fun handleEvent(event: AuthenticationViewModel.AuthenticationEvent) =
        when (event) {
            is AuthenticationViewModel.AuthenticationEvent.Error ->
                showToast(event.message)
        }
}
