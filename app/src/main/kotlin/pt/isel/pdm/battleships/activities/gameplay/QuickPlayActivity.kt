package pt.isel.pdm.battleships.activities.gameplay

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
import pt.isel.pdm.battleships.ui.screens.gameplay.QuickPlayScreen
import pt.isel.pdm.battleships.ui.theme.BattleshipsTheme
import pt.isel.pdm.battleships.viewModels.gameplay.QuickPlayViewModel

/**
 * Activity for the quick play screen.
 *
 * @property battleshipsService the service used to handle the battleships game
 * @property sessionManager the session manager used to handle the user session
 * @property jsonFormatter the json formatter
 * @property viewModel the view model used to handle the quick play process
 */
class QuickPlayActivity : ComponentActivity() {

    private val battleshipsService by lazy {
        (application as DependenciesContainer).battleshipsService
    }

    private val sessionManager by lazy {
        (application as DependenciesContainer).sessionManager
    }

    private val jsonFormatter by lazy {
        (application as DependenciesContainer).jsonFormatter
    }

    @Suppress("UNCHECKED_CAST")
    private val viewModel by viewModels<QuickPlayViewModel> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return QuickPlayViewModel(
                    sessionManager = sessionManager,
                    gamesService = battleshipsService.gamesService,
                    jsonFormatter = jsonFormatter,
                    assetManager = assets
                ) as T
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
                    QuickPlayScreen(viewModel)
                }
            }
        }
    }
}
