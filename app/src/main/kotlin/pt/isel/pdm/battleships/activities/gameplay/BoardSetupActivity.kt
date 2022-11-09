package pt.isel.pdm.battleships.activities.gameplay

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Text
import androidx.compose.runtime.LaunchedEffect
import pt.isel.pdm.battleships.DependenciesContainer
import pt.isel.pdm.battleships.activities.utils.navigateTo
import pt.isel.pdm.battleships.activities.utils.viewModelInit
import pt.isel.pdm.battleships.domain.ship.ShipType
import pt.isel.pdm.battleships.ui.screens.gameplay.newGame.boardSetup.BoardSetupScreen
import pt.isel.pdm.battleships.viewModels.gameplay.BoardSetupViewModel

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
        BoardSetupViewModel(battleshipsService, sessionManager)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val gameLink = intent.getStringExtra("gameLink")
            ?: throw IllegalStateException("No game link found")

        viewModel.loadGame(gameLink)

        setContent {
            LaunchedEffect(viewModel.state) {
                if (viewModel.state == BoardSetupViewModel.BoardSetupState.FLEET_DEPLOYED) {
                    navigateTo<GameplayActivity> {
                        it.putExtra("gameLink", gameLink)
                    }
                }
            }

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
                            // TODO fix this shit
//                            viewModel.deployFleet(board.fleet)
                        },
                        onBackButtonClicked = { finish() }
                    )
                }
            }
        }
    }
}
