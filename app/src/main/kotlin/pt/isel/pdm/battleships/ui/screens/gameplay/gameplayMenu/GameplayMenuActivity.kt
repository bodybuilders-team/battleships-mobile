package pt.isel.pdm.battleships.ui.screens.gameplay.gameplayMenu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import pt.isel.pdm.battleships.DependenciesContainer
import pt.isel.pdm.battleships.ui.screens.gameplay.gameConfiguration.GameConfigurationActivity
import pt.isel.pdm.battleships.ui.screens.gameplay.gameplayMenu.GameplayMenuViewModel.Event
import pt.isel.pdm.battleships.ui.screens.gameplay.lobby.LobbyActivity
import pt.isel.pdm.battleships.ui.screens.gameplay.matchmake.MatchmakeActivity
import pt.isel.pdm.battleships.ui.utils.ToastDuration
import pt.isel.pdm.battleships.ui.utils.showToast
import pt.isel.pdm.battleships.utils.Links.Companion.getLinks
import pt.isel.pdm.battleships.utils.navigateWithLinksTo
import pt.isel.pdm.battleships.utils.viewModelInit

/**
 * Activity for the gameplay menu screen.
 */
class GameplayMenuActivity : ComponentActivity() {

    private val battleshipsService by lazy {
        (application as DependenciesContainer).battleshipsService
    }

    val viewModel by viewModelInit {
        GameplayMenuViewModel(
            usersService = battleshipsService.usersService
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val links = intent.getLinks()

        val userHomeLink =
            links["user-home"] ?: throw IllegalStateException("User home link not found")

        viewModel.loadUserHome(userHomeLink)

        lifecycleScope.launch {
            viewModel.events.collect {
                handleEvent(it, userHomeLink)
            }
        }

        setContent {
            GameplayMenuScreen(
                viewModel.loadingState,
                onMatchmakeClick = {
                    viewModel.navigateTo<MatchmakeActivity>(setOf("matchmake"))
                },
                onCreateGameClick = {
                    viewModel.navigateTo<GameConfigurationActivity>(setOf("create-game"))
                },
                onLobbyClick = {
                    viewModel.navigateTo<LobbyActivity>(setOf("list-games"))
                },
                onBackButtonClick = { finish() }
            )
        }
    }

    /**
     * Handles the events emitted by the view model.
     */
    private suspend fun handleEvent(event: Event, userHomeLink: String) =
        when (event) {
            is Event.Navigate -> {
                val links =
                    event.linkRels?.let { rels ->
                        viewModel.links
                            .filter { rels.contains(it.key) }
                            .mapValues { it.value }
                    }

                navigateWithLinksTo(event.clazz, links)
            }
            is Event.Error -> {
                showToast(event.message, ToastDuration.LONG) {
                    viewModel.loadUserHome(userHomeLink)
                }
            }
        }
}
