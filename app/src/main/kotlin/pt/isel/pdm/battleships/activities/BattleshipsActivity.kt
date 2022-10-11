package pt.isel.pdm.battleships.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import pt.isel.pdm.battleships.DependenciesContainer
import pt.isel.pdm.battleships.ui.screens.MainMenuScreen
import pt.isel.pdm.battleships.ui.theme.BattleshipsTheme

/**
 * This activity is the main entry point of the application.
 * It is responsible for creating the main view and the view model.
 *
 * @property battleshipsService The service used to handle the battleships game.
 * @property vm The view model used to handle the application logic.
 */
class BattleshipsActivity : ComponentActivity() {

    private val battleshipsService by lazy {
        (application as DependenciesContainer).battleshipsService
    }

    private val sessionManager by lazy {
        (application as DependenciesContainer).sessionManager
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            BattleshipsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MainMenuScreen(showAuthentication = sessionManager.token == null)
                }
            }
        }
    }
}
