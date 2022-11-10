package pt.isel.pdm.battleships.ui.screens.gameplay.gameConfiguration

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pt.isel.pdm.battleships.domain.games.game.GameConfig
import pt.isel.pdm.battleships.services.games.GamesService
import pt.isel.pdm.battleships.services.utils.HTTPResult
import pt.isel.pdm.battleships.services.utils.siren.EmbeddedLink
import pt.isel.pdm.battleships.ui.screens.gameplay.gameConfiguration.GameConfigurationViewModel.GameConfigurationState.CREATING_GAME
import pt.isel.pdm.battleships.ui.screens.gameplay.gameConfiguration.GameConfigurationViewModel.GameConfigurationState.ERROR
import pt.isel.pdm.battleships.ui.screens.gameplay.gameConfiguration.GameConfigurationViewModel.GameConfigurationState.GAME_CREATED
import pt.isel.pdm.battleships.ui.screens.gameplay.gameConfiguration.GameConfigurationViewModel.GameConfigurationState.IDLE

/**
 * View model for the GameConfigurationActivity.
 *
 * @property gamesService the service that handles the games
 *
 * @property state the current state of the view model
 * @property gameLink the link to the game
 * @property errorMessage the error message to be displayed
 */
class GameConfigurationViewModel(
    private val gamesService: GamesService
) : ViewModel() {

    var state by mutableStateOf(IDLE)
    var gameLink: String? by mutableStateOf(null)
    var errorMessage: String? by mutableStateOf(null)

    /**
     * Creates a new game.
     *
     * @param createGameLink the link to the create game endpoint
     * @param gameConfig the game configuration
     */
    fun createGame(createGameLink: String, gameConfig: GameConfig) {
        state = CREATING_GAME

        viewModelScope.launch {
            when (val res = gamesService.createGame(createGameLink, gameConfig.toDTO())) {
                is HTTPResult.Success -> {
                    val entities = res.data.entities
                        ?: throw IllegalStateException("No entities in response")

                    gameLink = entities
                        .filterIsInstance<EmbeddedLink>()
                        .first { it.rel.contains("game") }.href.path

                    state = GAME_CREATED
                }
                is HTTPResult.Failure -> {
                    errorMessage = res.error.title
                    state = ERROR
                }
            }
        }
    }

    /**
     * Represents the game configuration state.
     *
     * @property IDLE the game configuration operation is idle
     * @property CREATING_GAME creating a new game
     * @property GAME_CREATED the game was created
     * @property ERROR an error occurred
     */
    enum class GameConfigurationState {
        IDLE,
        CREATING_GAME,
        GAME_CREATED,
        ERROR
    }
}
