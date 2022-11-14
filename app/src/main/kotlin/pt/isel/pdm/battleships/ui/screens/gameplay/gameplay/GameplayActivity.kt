package pt.isel.pdm.battleships.ui.screens.gameplay.gameplay

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Text
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import pt.isel.pdm.battleships.DependenciesContainer
import pt.isel.pdm.battleships.ui.utils.showToast
import pt.isel.pdm.battleships.ui.utils.viewModelInit

/**
 * Activity for the gameplay screen.
 *
 * @property battleshipsService the service used to handle the battleships game
 * @property sessionManager the session manager used to handle the user session
 * @property jsonEncoder the json formatter
 */
class GameplayActivity : ComponentActivity() {

    val battleshipsService by lazy {
        (application as DependenciesContainer).battleshipsService
    }

    val sessionManager by lazy {
        (application as DependenciesContainer).sessionManager
    }

    val jsonEncoder by lazy {
        (application as DependenciesContainer).jsonEncoder
    }

    private val viewModel by viewModelInit {
        GameplayViewModel(
            battleshipsService = battleshipsService,
            sessionManager = sessionManager
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val gameLink = intent.getStringExtra("gameLink")
            ?: throw IllegalStateException("No game link found")

        lifecycleScope.launch {
            viewModel.events.collect {
                handleEvent(it)
            }
        }

        viewModel.loadGame(gameLink)

        setContent {
            when (viewModel.screenState.state) {
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

    private suspend fun handleEvent(event: GameplayViewModel.GameplayEvent) =
        when (event) {
            is GameplayViewModel.GameplayEvent.Error -> {
                showToast(event.message)
            }
        }
}
