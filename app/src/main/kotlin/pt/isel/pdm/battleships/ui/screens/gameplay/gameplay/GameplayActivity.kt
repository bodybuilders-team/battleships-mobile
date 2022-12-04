package pt.isel.pdm.battleships.ui.screens.gameplay.gameplay

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.material.Text
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import pt.isel.pdm.battleships.R
import pt.isel.pdm.battleships.ui.screens.BattleshipsActivity
import pt.isel.pdm.battleships.ui.screens.gameplay.gameplay.GameplayViewModel.GameplayState.FINISHED_GAME
import pt.isel.pdm.battleships.ui.screens.gameplay.gameplay.GameplayViewModel.GameplayState.IDLE
import pt.isel.pdm.battleships.ui.screens.gameplay.gameplay.GameplayViewModel.GameplayState.LINKS_LOADED
import pt.isel.pdm.battleships.ui.screens.gameplay.gameplay.GameplayViewModel.GameplayState.LOADING_GAME
import pt.isel.pdm.battleships.ui.screens.gameplay.gameplay.GameplayViewModel.GameplayState.LOADING_MY_FLEET
import pt.isel.pdm.battleships.ui.utils.Event
import pt.isel.pdm.battleships.ui.utils.components.LoadingSpinner
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
                IDLE, LINKS_LOADED, LOADING_GAME -> {
                    LoadingSpinner("Loading Game..")
                }
                LOADING_MY_FLEET -> {
                    LoadingSpinner("Loading Fleet..")
                }
                FINISHED_GAME -> {
                    Text("Game Finished")
                }
                else -> {
                    GameplayScreen(
                        myTurn = viewModel.screenState.myTurn
                            ?: throw IllegalStateException("My turn not found"),
                        myBoard = viewModel.screenState.myBoard
                            ?: throw IllegalStateException("My board not found"),
                        opponentBoard = viewModel.screenState.opponentBoard
                            ?: throw IllegalStateException("Opponent board not found"),
                        gameConfig = viewModel.screenState.gameConfig
                            ?: throw IllegalStateException("Game config not found"),
                        gameState = viewModel.screenState.gameState
                            ?: throw IllegalStateException("Game state not found"),
                        playerInfo = PlayerInfo("Jesus", R.drawable.andre_jesus),
                        opponentInfo = PlayerInfo("Nyck", R.drawable.nyckollas_brandao),
                        onShootClicked = { coordinates ->
                            viewModel.fireShots(coordinates)
                        },
                        onBackButtonClicked = { finish() },
                        onPlayAgainButtonClicked = { finish() },
                        onBackToMenuButtonClicked = { /*TODO*/ }
                    )
                }
            }
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
        }
    }
}
