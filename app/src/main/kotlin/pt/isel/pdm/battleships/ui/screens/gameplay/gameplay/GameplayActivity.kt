package pt.isel.pdm.battleships.ui.screens.gameplay.gameplay

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.google.gson.stream.JsonReader
import pt.isel.pdm.battleships.DependenciesContainer
import pt.isel.pdm.battleships.domain.games.board.MyBoard
import pt.isel.pdm.battleships.domain.games.game.GameConfig
import pt.isel.pdm.battleships.services.games.dtos.GameConfigDTO

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val gameLink = intent.getStringExtra("gameLink")
                ?: throw IllegalStateException("No game link found")

            val myBoard = MyBoard()

            val gameConfig = GameConfig(
                jsonEncoder.fromJson(
                    JsonReader(assets.open("defaultGameConfig.json").reader()),
                    GameConfigDTO::class.java
                )
            )

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
