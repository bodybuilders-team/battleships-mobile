package pt.isel.pdm.battleships.ui.screens.gameplay.lobby

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import pt.isel.pdm.battleships.ui.screens.BattleshipsActivity
import pt.isel.pdm.battleships.ui.screens.gameplay.boardSetup.BoardSetupActivity
import pt.isel.pdm.battleships.ui.screens.gameplay.lobby.LobbyViewModel.LobbyState.IDLE
import pt.isel.pdm.battleships.ui.screens.shared.Event
import pt.isel.pdm.battleships.ui.screens.shared.navigation.Links.Companion.getLinks
import pt.isel.pdm.battleships.ui.screens.shared.navigation.navigateWithLinksTo
import pt.isel.pdm.battleships.ui.screens.shared.showToast

/**
 * Activity for the [LobbyScreen].
 */
class LobbyActivity : BattleshipsActivity() {

    private val viewModel by getViewModel(::LobbyViewModel)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            viewModel.events.collect {
                handleEvent(it)
            }
        }

        if (viewModel.state == IDLE) {
            viewModel.updateLinks(intent.getLinks())
            viewModel.getGames()
        }

        setContent {
            LobbyScreen(
                state = viewModel.state,
                games = viewModel.games,
                playerName = viewModel.playerName
                    ?: throw IllegalStateException("Player name cannot be null"),
                onJoinGameRequest = { gameLink -> viewModel.joinGame(gameLink) },
                onBackButtonClicked = { finish() }
            )
        }
    }

    /**
     * Handles the specified event.
     *
     * @param event the event to handle
     */
    private suspend fun handleEvent(event: Event) {
        when (event) {
            is Event.Error -> showToast(event.message)
            is LobbyViewModel.LobbyEvent.NavigateToBoardSetup -> {
                navigateWithLinksTo<BoardSetupActivity>(links = viewModel.getLinks())
                finish()
            }
        }
    }
}
