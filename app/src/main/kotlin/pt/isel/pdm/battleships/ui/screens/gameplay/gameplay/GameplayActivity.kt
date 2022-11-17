package pt.isel.pdm.battleships.ui.screens.gameplay.gameplay

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.material.Text
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import pt.isel.pdm.battleships.ui.screens.shared.BattleshipsActivity
import pt.isel.pdm.battleships.ui.screens.shared.BattleshipsViewModel.BattleshipsState.Companion.IDLE
import pt.isel.pdm.battleships.ui.utils.Event
import pt.isel.pdm.battleships.ui.utils.navigation.Links.Companion.getLinks
import pt.isel.pdm.battleships.ui.utils.showToast

/**
 * Activity for the gameplay screen.
 */
class GameplayActivity : BattleshipsActivity() {

    private val viewModel by getViewModel(::GameplayViewModel)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            viewModel.events.collect {
                handleEvent(it)
            }
        }

        if (viewModel.state == IDLE) {
            viewModel.updateLinks(intent.getLinks())
            viewModel.loadGame()
        }

        setContent {
            when (viewModel.state) {
                GameplayViewModel.GameplayState.LOADING_GAME -> {
                    Text("Loading Game..")
                }
                GameplayViewModel.GameplayState.LOADING_MY_FLEET -> {
                    Text("Loading Fleet..")
                }
                GameplayViewModel.GameplayState.FINISHED_GAME -> {
                    Text("Game Finished")
                }
                else -> {
                    GameplayScreen(
                        round = 0,
                        myTurn = viewModel.screenState.myTurn
                            ?: throw IllegalStateException("My turn not found"),
                        myBoard = viewModel.screenState.myBoard
                            ?: throw IllegalStateException("My board not found"),
                        opponentBoard = viewModel.screenState.opponentBoard
                            ?: throw IllegalStateException("Opponent board not found"),
                        gameConfig = viewModel.screenState.gameConfig
                            ?: throw IllegalStateException("Game config not found"),
                        onShootClicked = { coordinates ->
                            viewModel.fireShots(coordinates)
                        },
                        onBackButtonClicked = { finish() }
                    )
                }
            }
        }
    }

    private suspend fun handleEvent(event: Event) {
        when (event) {
            is Event.Error -> showToast(event.message)
        }
    }
}
