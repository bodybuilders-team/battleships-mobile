package pt.isel.pdm.battleships.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import pt.isel.pdm.battleships.DependenciesContainer
import pt.isel.pdm.battleships.ui.screens.authentication.RegisterScreen
import pt.isel.pdm.battleships.ui.theme.BattleshipsTheme
import pt.isel.pdm.battleships.viewModels.authentication.RegisterViewModel

/**
 * This activity is used for creating a new user in the application.
 *
 * @property viewModel The view model used to handle the login process.
 * @property battleshipsService The service used to handle the battleships game.
 * @property sessionManager The session manager used to handle the user session.
 */
class RegisterActivity : ComponentActivity() {

    private val battleshipsService by lazy {
        (application as DependenciesContainer).battleshipsService
    }

    private val sessionManager by lazy {
        (application as DependenciesContainer).sessionManager
    }

    @Suppress("UNCHECKED_CAST")
    private val viewModel by viewModels<RegisterViewModel> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return RegisterViewModel(sessionManager, battleshipsService.usersService) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            BattleshipsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    RegisterScreen(
                        viewModel,
                        onBackButtonClicked = {
                            finish()
                        }
                    )
                }
            }
        }
    }
}
