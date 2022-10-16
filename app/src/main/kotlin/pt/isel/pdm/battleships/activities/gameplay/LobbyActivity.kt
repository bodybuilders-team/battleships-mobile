package pt.isel.pdm.battleships.activities.gameplay

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
import pt.isel.pdm.battleships.ui.screens.gameplay.lobby.LobbyScreen
import pt.isel.pdm.battleships.ui.theme.BattleshipsTheme
import pt.isel.pdm.battleships.viewModels.gameplay.LobbyViewModel

/**
 * Activity for the lobby screen.
 *
 * @property battleshipsService the service used to handle the battleships game
 * @property sessionManager the session manager used to handle the user session
 * @property viewModel the view model used to the search game process
 */
class LobbyActivity : ComponentActivity() {

    private val battleshipsService by lazy {
        (application as DependenciesContainer).battleshipsService
    }

    private val sessionManager by lazy {
        (application as DependenciesContainer).sessionManager
    }

    private val viewModel: LobbyViewModel by viewModels {
        viewModelInit {
            LobbyViewModel(
                sessionManager = sessionManager,
                gamesService = battleshipsService.gamesService
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
                    LobbyScreen(
                        viewModel,
                        onBackButtonClicked = { finish() }
                    )
                }
            }
        }
    }
}
