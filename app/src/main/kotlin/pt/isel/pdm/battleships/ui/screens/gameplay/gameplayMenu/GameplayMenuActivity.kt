package pt.isel.pdm.battleships.ui.screens.gameplay.gameplayMenu

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import pt.isel.pdm.battleships.ui.screens.gameplay.gameConfiguration.GameConfigurationActivity
import pt.isel.pdm.battleships.ui.screens.gameplay.lobby.LobbyActivity
import pt.isel.pdm.battleships.ui.screens.gameplay.quickPlay.QuickPlayActivity
import pt.isel.pdm.battleships.utils.Links.Companion.LINKS_KEY
import pt.isel.pdm.battleships.utils.Links.Companion.getLinks
import pt.isel.pdm.battleships.utils.navigateTo

/**
 * Activity for the gameplay menu screen.
 */
class GameplayMenuActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val links = intent.getLinks()

        Log.v("GameplayMenuActivityLog", "links: $links")
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
