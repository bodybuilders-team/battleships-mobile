package pt.isel.pdm.battleships.activities.gameplay

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import pt.isel.pdm.battleships.DependenciesContainer
import pt.isel.pdm.battleships.activities.utils.navigateTo
import pt.isel.pdm.battleships.ui.screens.gameplay.newGame.GameConfigurationScreen
import pt.isel.pdm.battleships.ui.theme.BattleshipsTheme

/**
 * Activity for the new game screen.
 */
class GameConfigurationActivity : ComponentActivity() {

    private val battleshipsService by lazy {
        (application as DependenciesContainer).battleshipsService
    }

//    private val viewModel by viewModelInit { GameConfigurationViewModel() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            BattleshipsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    GameConfigurationScreen(
                        onGameConfigured = { gameConfig ->
                            // Api call to add game to lobby: intent.putExtra("gameId", gameId)
                            navigateTo<BoardSetupActivity> {
                                it.putExtra("gameConfig", gameConfig)
                            }
                        },
                        onBackButtonClicked = { finish() }
                    )
                }
            }
        }
    }
}
