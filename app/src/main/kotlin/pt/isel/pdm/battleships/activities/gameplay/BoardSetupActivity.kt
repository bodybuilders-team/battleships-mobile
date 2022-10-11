package pt.isel.pdm.battleships.activities.gameplay

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import pt.isel.pdm.battleships.domain.game.GameConfig
import pt.isel.pdm.battleships.ui.screens.gameplay.newGame.boardSetup.BoardSetupScreen
import pt.isel.pdm.battleships.ui.theme.BattleshipsTheme

class BoardSetupActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            BattleshipsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val gameConfig =
                        intent.getSerializableExtra("gameConfig") as GameConfig

                    BoardSetupScreen(
                        boardSize = gameConfig.gridSize,
                        ships = gameConfig.ships,
                        onBoardSetupFinished = { board ->
                            val intent = Intent(this, GameplayActivity::class.java)
                            intent.putExtra("board", board)
                            intent.putExtra("gameConfig", gameConfig)

                            startActivity(intent)
                        },
                        onBackButtonClicked = {
                            finish()
                        }
                    )
                }
            }
        }
    }
}
