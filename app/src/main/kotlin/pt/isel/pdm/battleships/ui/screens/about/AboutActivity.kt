package pt.isel.pdm.battleships.ui.screens.about

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

/**
 * Activity for the about screen.
 */
class AboutActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AboutScreen(onBackButtonClicked = { finish() })
        }
    }
}
