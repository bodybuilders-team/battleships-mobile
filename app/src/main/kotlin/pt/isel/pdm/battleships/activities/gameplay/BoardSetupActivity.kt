package pt.isel.pdm.battleships.activities.gameplay

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import pt.isel.pdm.battleships.DependenciesContainer
import pt.isel.pdm.battleships.activities.utils.viewModelInit
import pt.isel.pdm.battleships.domain.game.GameConfig
import pt.isel.pdm.battleships.ui.screens.gameplay.newGame.boardSetup.BoardSetupScreen
import pt.isel.pdm.battleships.ui.theme.BattleshipsTheme
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

        setContent {
            BattleshipsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val gameConfig = intent.getSerializableExtra("gameConfig") as GameConfig

                    BoardSetupScreen(
                        boardSize = gameConfig.gridSize,
                        ships = gameConfig.ships,
                        onBoardSetupFinished = { board ->
                            val intent = Intent(this, GameplayActivity::class.java)
                            intent.putExtra("board", board.toMyBoard())
                            intent.putExtra("gameConfig", gameConfig)

                            startActivity(intent)
                        },
                        onBackButtonClicked = { finish() }
                    )
                }
            }
        }
    }
}
