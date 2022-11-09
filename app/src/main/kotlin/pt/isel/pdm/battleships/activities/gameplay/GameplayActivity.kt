package pt.isel.pdm.battleships.activities.gameplay

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import pt.isel.pdm.battleships.domain.board.MyBoard
import pt.isel.pdm.battleships.domain.game.GameConfig
import pt.isel.pdm.battleships.ui.screens.gameplay.GameplayScreen

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
