package pt.isel.pdm.battleships.ui.screens.gameplay.gameplay

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import pt.isel.pdm.battleships.domain.games.board.MyBoard
import pt.isel.pdm.battleships.domain.games.game.GameConfig

/**
 * Activity for the gameplay screen.
 */
class GameplayActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val myBoard = intent.getParcelableExtra<MyBoard>("board")
                ?: throw IllegalArgumentException("Board not found")

            val gameConfig = intent.getParcelableExtra<GameConfig>("gameConfig")
                ?: throw IllegalArgumentException("GameConfig not found")

            GameplayScreen(
                myBoard = myBoard,
                gameConfig = gameConfig,
                onShootClicked = { coordinates ->
                    // vm.shoot(coordinates)
                },
                onBackButtonClicked = { finish() }
            )
        }
    }
}
