package pt.isel.pdm.battleships.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import pt.isel.pdm.battleships.ui.screens.ranking.RankingScreen

/**
 * Activity for the ranking screen.
 */
class RankingActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            RankingScreen(onBackButtonClicked = { finish() })
        }
    }
}
