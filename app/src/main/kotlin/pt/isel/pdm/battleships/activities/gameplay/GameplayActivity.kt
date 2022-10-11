package pt.isel.pdm.battleships.activities.gameplay

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import pt.isel.pdm.battleships.domain.board.Board
import pt.isel.pdm.battleships.domain.game.GameConfig
import pt.isel.pdm.battleships.ui.screens.gameplay.GameplayScreen
import pt.isel.pdm.battleships.ui.theme.BattleshipsTheme

class GameplayActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            BattleshipsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val board = intent.getSerializableExtra("board") as Board
                    val gameConfig = intent.getSerializableExtra("gameConfig") as GameConfig

                    GameplayScreen(
                        board = board,
                        gameConfig = gameConfig,
                        onBackButtonClicked = { finish() }
                    )
                }
            }
        }
    }
}
