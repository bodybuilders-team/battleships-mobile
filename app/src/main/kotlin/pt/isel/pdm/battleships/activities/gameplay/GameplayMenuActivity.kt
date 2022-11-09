package pt.isel.pdm.battleships.activities.gameplay

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import pt.isel.pdm.battleships.activities.utils.LINKS_KEY
import pt.isel.pdm.battleships.activities.utils.getLinks
import pt.isel.pdm.battleships.activities.utils.navigateTo
import pt.isel.pdm.battleships.activities.utils.subSet
import pt.isel.pdm.battleships.ui.screens.gameplay.GameplayMenuScreen

/**
 * Activity for the gameplay menu screen.
 */
class GameplayMenuActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val links = intent.getLinks()

        setContent {
            GameplayMenuScreen(
                onQuickPlayClick = {
                    navigateTo<QuickPlayActivity> {
                        it.putExtra(LINKS_KEY, links.subSet("matchmake"))
                    }
                },
                onCreateGameClick = {
                    navigateTo<GameConfigurationActivity> {
                        it.putExtra(LINKS_KEY, links.subSet("create-game"))
                    }
                },
                onLobbyClick = {
                    navigateTo<LobbyActivity> {
                        it.putExtra(LINKS_KEY, links.subSet("list-games"))
                    }
                },
                onBackButtonClick = { finish() }
            )
        }
    }
}
