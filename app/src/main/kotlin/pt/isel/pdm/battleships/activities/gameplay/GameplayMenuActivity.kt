package pt.isel.pdm.battleships.activities.gameplay

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import pt.isel.pdm.battleships.ui.screens.gameplay.GameplayMenuScreen
import pt.isel.pdm.battleships.ui.theme.BattleshipsTheme

/**
 * Activity for the gameplay menu screen.
 */
class GameplayMenuActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            BattleshipsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    GameplayMenuScreen(onBackButtonClicked = { finish() })
                }
            }
        }
    }
}