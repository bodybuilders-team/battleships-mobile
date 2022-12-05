package pt.isel.pdm.battleships.ui.screens.gameplay.gameplayMenu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import pt.isel.pdm.battleships.ui.screens.gameplay.createGame.CreateGameActivity
import pt.isel.pdm.battleships.ui.screens.gameplay.lobby.LobbyActivity
import pt.isel.pdm.battleships.ui.screens.gameplay.matchmake.MatchmakeActivity
import pt.isel.pdm.battleships.ui.screens.shared.navigation.Links.Companion.getLinks
import pt.isel.pdm.battleships.ui.screens.shared.navigation.navigateWithLinksTo

/**
 * Activity for the gameplay menu screen.
 */
class GameplayMenuActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val links = intent.getLinks()

        setContent {
            GameplayMenuScreen(
                onMatchmakeClick = { navigateWithLinksTo<MatchmakeActivity>(links) },
                onCreateGameClick = { navigateWithLinksTo<CreateGameActivity>(links) },
                onLobbyClick = { navigateWithLinksTo<LobbyActivity>(links) },
                onBackButtonClick = { finish() }
            )
        }
    }
}
