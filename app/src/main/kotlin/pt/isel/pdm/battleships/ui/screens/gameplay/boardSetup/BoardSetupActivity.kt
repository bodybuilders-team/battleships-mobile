package pt.isel.pdm.battleships.ui.screens.gameplay.boardSetup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Text
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import pt.isel.pdm.battleships.DependenciesContainer
import pt.isel.pdm.battleships.domain.games.ship.ShipType
import pt.isel.pdm.battleships.ui.screens.gameplay.gameplay.GameplayActivity
import pt.isel.pdm.battleships.ui.utils.showToast
import pt.isel.pdm.battleships.utils.navigateTo
import pt.isel.pdm.battleships.utils.viewModelInit

/**
 * Activity for the board setup screen.
 */
class BoardSetupActivity : ComponentActivity() {

    private val battleshipsService by lazy {
        (application as DependenciesContainer).battleshipsService
    }

    private val sessionManager by lazy {
        (application as DependenciesContainer).sessionManager
    }

    private val viewModel by viewModelInit {
        BoardSetupViewModel(
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
                handleEvent(it, gameLink)
            }
        }

        viewModel.loadGame(gameLink)

        setContent {
            when (viewModel.state) {
                BoardSetupViewModel.BoardSetupState.LOADING_GAME -> {
                    Text("Loading Game..")
                }
                else -> {
                    val game = viewModel.game
                        ?: throw IllegalStateException("No game found")
                    val properties =
                        game.properties ?: throw IllegalStateException("No game properties found")

                    val gridSize = properties.config.gridSize
                    val ships = properties.config.shipTypes.map {
                        ShipType.fromDTO(
                            it
                        )
                    }

                    BoardSetupScreen(
                        boardSize = gridSize,
                        ships = ships,
                        onBoardSetupFinished = { board ->
                            val deployFleetLink =
                                game.actions?.find { it.name == "deployFleet" }?.href?.path
                                    ?: throw IllegalStateException("No deploy fleet link found")

                            viewModel.deployFleet(deployFleetLink, board.fleet)
                        },
                        onBackButtonClicked = { finish() }
                    )
                }
            }
        }
    }

    private suspend fun handleEvent(event: BoardSetupViewModel.BoardSetupEvent, gameLink: String) =
        when (event) {
            is BoardSetupViewModel.BoardSetupEvent.NavigateToGameplay -> {
                navigateTo<GameplayActivity> {
                    it.putExtra("gameLink", gameLink)
                }
                finish()
            }

            is BoardSetupViewModel.BoardSetupEvent.Error -> {
                showToast(event.message)
            }
        }
}
