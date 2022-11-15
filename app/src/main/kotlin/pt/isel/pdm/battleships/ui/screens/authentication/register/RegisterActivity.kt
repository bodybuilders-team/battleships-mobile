package pt.isel.pdm.battleships.ui.screens.authentication.register

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import pt.isel.pdm.battleships.DependenciesContainer
import pt.isel.pdm.battleships.ui.screens.authentication.AuthenticationViewModel.AuthenticationState
import pt.isel.pdm.battleships.ui.utils.Event
import pt.isel.pdm.battleships.ui.utils.navigation.Links.Companion.getLinks
import pt.isel.pdm.battleships.ui.utils.navigation.Links.Companion.putLinks
import pt.isel.pdm.battleships.ui.utils.showToast
import pt.isel.pdm.battleships.ui.utils.viewModelInit

/**
 * Activity for the register screen.
 *
 * @property viewModel the view model used to handle the register process
 */
class RegisterActivity : ComponentActivity() {

    val dependenciesContainer by lazy {
        (application as DependenciesContainer)
    }

    private val viewModel by viewModelInit {
        RegisterViewModel(
            battleshipsService = dependenciesContainer.battleshipsService,
            sessionManager = dependenciesContainer.sessionManager
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            viewModel.events.collect {
                handleEvent(it)
            }
        }

        if (viewModel.state == AuthenticationState.IDLE) {
            viewModel.updateLinks(intent.getLinks())
        }

        setContent {
            RegisterScreen(
                state = viewModel.state,
                onRegister = { email, username, password ->
                    viewModel.register(
                        email = email,
                        username = username,
                        password = password
                    )
                },
                onRegisterSuccessful = {
                    val resultIntent = Intent()

                    resultIntent.putLinks(viewModel.getLinks())

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
     * Handles the given event.
     *
     * @param event the event to handle
     */
    private suspend fun handleEvent(event: Event) {
        when (event) {
            is Event.Error -> showToast(event.message)
        }
    }
}
