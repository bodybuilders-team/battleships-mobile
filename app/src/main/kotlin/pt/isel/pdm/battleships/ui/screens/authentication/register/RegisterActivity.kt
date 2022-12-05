package pt.isel.pdm.battleships.ui.screens.authentication.register

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import pt.isel.pdm.battleships.ui.screens.BattleshipsActivity
import pt.isel.pdm.battleships.ui.screens.authentication.AuthenticationViewModel.AuthenticationState.IDLE
import pt.isel.pdm.battleships.ui.screens.shared.Event
import pt.isel.pdm.battleships.ui.screens.shared.navigation.Links.Companion.getLinks
import pt.isel.pdm.battleships.ui.screens.shared.navigation.Links.Companion.putLinks
import pt.isel.pdm.battleships.ui.screens.shared.showToast

/**
 * Activity for the register screen.
 *
 * @property viewModel the view model used to handle the register process
 */
class RegisterActivity : BattleshipsActivity() {

    private val viewModel by getViewModel(::RegisterViewModel)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            viewModel.events.collect {
                handleEvent(it)
            }
        }

        if (viewModel.state == IDLE)
            viewModel.updateLinks(intent.getLinks())

        setContent {
            RegisterScreen(
                state = viewModel.state,
                onRegister = { email, username, password ->
                    viewModel.register(email = email, username = username, password = password)
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
     * Handles the specified event.
     *
     * @param event the event to handle
     */
    private suspend fun handleEvent(event: Event) {
        when (event) {
            is Event.Error -> showToast(event.message)
        }
    }
}
