package pt.isel.pdm.battleships.activities.authentication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import pt.isel.pdm.battleships.DependenciesContainer
import pt.isel.pdm.battleships.activities.utils.viewModelInit
import pt.isel.pdm.battleships.ui.screens.authentication.register.RegisterScreen
import pt.isel.pdm.battleships.ui.theme.BattleshipsTheme
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

    private val viewModel: RegisterViewModel by viewModels {
        viewModelInit {
            RegisterViewModel(sessionManager, battleshipsService.usersService)
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
                        onBackButtonClicked = { finish() }
                    )
                }
            }
        }
    }
}
