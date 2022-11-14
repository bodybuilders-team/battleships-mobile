package pt.isel.pdm.battleships.ui.screens.authentication.login

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import pt.isel.pdm.battleships.DependenciesContainer
import pt.isel.pdm.battleships.ui.utils.Event
import pt.isel.pdm.battleships.ui.utils.navigation.Links
import pt.isel.pdm.battleships.ui.utils.navigation.Links.Companion.LINKS_KEY
import pt.isel.pdm.battleships.ui.utils.navigation.Links.Companion.getLinks
import pt.isel.pdm.battleships.ui.utils.navigation.Rels
import pt.isel.pdm.battleships.ui.utils.navigation.Rels.LOGIN
import pt.isel.pdm.battleships.ui.utils.showToast
import pt.isel.pdm.battleships.ui.utils.viewModelInit

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
        LoginViewModel(
            usersService = battleshipsService.usersService,
            sessionManager = sessionManager
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val links = intent.getLinks()

        val loginLink = links[LOGIN]
            ?: throw IllegalStateException("Login link not found")

        lifecycleScope.launch {
            viewModel.events.collect {
                handleEvent(it)
            }
        }

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
                    val userHomeLink = viewModel.link
                        ?: throw IllegalStateException("Link not found")

                    val resultIntent = Intent()
                    resultIntent.putExtra(LINKS_KEY, Links(mapOf(Rels.USER_HOME to userHomeLink)))

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

    /**
     * Handles the events emitted by the view model.
     *
     * @param event the event emitted by the view model
     */
    private suspend fun handleEvent(event: Event) {
        when (event) {
            is Event.Error -> showToast(event.message)
        }
    }
}
