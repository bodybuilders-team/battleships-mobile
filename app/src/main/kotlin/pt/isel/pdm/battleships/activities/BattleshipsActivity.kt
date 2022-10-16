package pt.isel.pdm.battleships.activities

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
import pt.isel.pdm.battleships.ui.screens.HomeScreen
import pt.isel.pdm.battleships.ui.theme.BattleshipsTheme
import pt.isel.pdm.battleships.viewModels.BattleshipsViewModel

/**
 * This activity is the main entry point of the application.
 * It is responsible for creating the main view and the view model.
 *
 * @property battleshipsService the service used to handle the battleships game
 * @property sessionManager the session manager used to handle the user session
 * @property jsonFormatter the json formatter used to format the json data
 * @property viewModel the view model used to handle the state of the application
 */
class BattleshipsActivity : ComponentActivity() {

    private val battleshipsService by lazy {
        (application as DependenciesContainer).battleshipsService
    }

    private val sessionManager by lazy {
        (application as DependenciesContainer).sessionManager
    }

    private val jsonFormatter by lazy {
        (application as DependenciesContainer).jsonFormatter
    }

    private val viewModel: BattleshipsViewModel by viewModels {
        viewModelInit {
            BattleshipsViewModel(
                battleshipsService,
                sessionManager,
                assets,
                jsonFormatter
            )
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
                    HomeScreen(showAuthentication = sessionManager.token == null)
                }
            }
        }
    }
}
