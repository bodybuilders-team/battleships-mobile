package pt.isel.pdm.battleships.ui.screens.gameplay.lobby

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import pt.isel.pdm.battleships.DependenciesContainer
import pt.isel.pdm.battleships.utils.Links.Companion.getLinks
import pt.isel.pdm.battleships.utils.Rels.LIST_GAMES
import pt.isel.pdm.battleships.utils.viewModelInit

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

    private val viewModel by viewModelInit {
        LobbyViewModel(
            sessionManager = sessionManager,
            gamesService = battleshipsService.gamesService
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val links = intent.getLinks()

        viewModel.getAllGames(
            links[LIST_GAMES]
                ?: throw IllegalStateException("No $LIST_GAMES link found")
        )

        setContent {
            LobbyScreen(
                state = viewModel.state,
                games = viewModel.games,
                errorMessage = viewModel.errorMessage,
                onBackButtonClicked = { finish() }
            )
        }
    }
}
