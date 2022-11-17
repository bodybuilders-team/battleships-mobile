package pt.isel.pdm.battleships.ui.screens.gameplay.lobby

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import pt.isel.pdm.battleships.ui.screens.gameplay.lobby.LobbyViewModel.LobbyState.IDLE
import pt.isel.pdm.battleships.ui.screens.shared.BattleshipsActivity
import pt.isel.pdm.battleships.ui.utils.Event
import pt.isel.pdm.battleships.ui.utils.navigation.Links.Companion.getLinks
import pt.isel.pdm.battleships.ui.utils.showToast

/**
 * Activity for the lobby screen.
 *
 * @property viewModel the view model used to the search game process
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
                onBackButtonClicked = { finish() }
            )
        }
    }

    /**
     * Handles the events emitted by the view model.
     *
     * @param event the event to be handled
     */
    private suspend fun handleEvent(event: Event) {
        when (event) {
            is Event.Error -> showToast(event.message)
        }
    }
}
