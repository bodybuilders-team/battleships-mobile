package pt.isel.pdm.battleships.ui.screens.gameplay.lobby

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import pt.isel.pdm.battleships.DependenciesContainer
import pt.isel.pdm.battleships.ui.screens.gameplay.lobby.LobbyViewModel.LobbyEvent
import pt.isel.pdm.battleships.ui.utils.navigation.Links.Companion.getLinks
import pt.isel.pdm.battleships.ui.utils.navigation.Rels.LIST_GAMES
import pt.isel.pdm.battleships.ui.utils.showToast
import pt.isel.pdm.battleships.ui.utils.viewModelInit

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

        val listGamesLink = links[LIST_GAMES]
            ?: throw IllegalStateException("No $LIST_GAMES link found")

        lifecycleScope.launch {
            viewModel.events.collect {
                handleEvent(it, listGamesLink)
            }
        }

        viewModel.getAllGames(listGamesLink)

        setContent {
            LobbyScreen(
                state = viewModel.state,
                games = viewModel.games,
                onBackButtonClicked = { finish() }
            )
        }
    }

    /**
     * Handles the events emitted by the view model.
     *
     * @param event the event to be handled
     * @param listGamesLink the link to the list of games endpoint
     */
    private suspend fun handleEvent(event: LobbyEvent, listGamesLink: String) =
        when (event) {
            is LobbyEvent.Error -> {
                showToast(event.message) {
                    viewModel.getAllGames(listGamesLink)
                }
            }
        }
}
