package pt.isel.pdm.battleships.activities.gameplay

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import pt.isel.pdm.battleships.ui.screens.gameplay.newGame.GameConfigurationScreen
import pt.isel.pdm.battleships.ui.theme.BattleshipsTheme

/**
 * Activity for the new game screen.
 */
class GameConfigurationActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            BattleshipsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    GameConfigurationScreen(
                        onGameConfigured = {
                            val intent = Intent(this, BoardSetupActivity::class.java)
                            // Api call to add game to lobby: intent.putExtra("gameId", gameId)

                            intent.putExtra("gameConfig", it)
                            startActivity(intent)
                        },
                        onBackButtonClicked = { finish() }
                    )
                }
            }
        }
    }
}
