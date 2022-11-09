package pt.isel.pdm.battleships.viewModels.gameplay

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pt.isel.pdm.battleships.domain.game.GameConfig
import pt.isel.pdm.battleships.services.games.GamesService
import pt.isel.pdm.battleships.services.utils.HTTPResult
import pt.isel.pdm.battleships.services.utils.siren.EmbeddedLink

class GameConfigurationViewModel(
    private val gamesService: GamesService
) : ViewModel() {
    var state by mutableStateOf(GameConfigurationState.IDLE)
    var gameLink: String? by mutableStateOf(null)
    var errorMessage: String? by mutableStateOf(null)

    fun createGame(createGameLink: String, gameConfig: GameConfig) {
        state = GameConfigurationState.CREATING_GAME

        viewModelScope.launch {
            when (val res = gamesService.createGame(createGameLink, gameConfig.toDTO())) {
                is HTTPResult.Success -> {
                    val entities =
                        res.data.entities ?: throw IllegalStateException("No entities in response")

                    gameLink = entities
                        .filterIsInstance<EmbeddedLink>()
                        .first { it.rel.contains("game") }.href.path

                    state = GameConfigurationState.GAME_CREATED
                }
                is HTTPResult.Failure -> {
                    errorMessage = res.error.message
                    state = GameConfigurationState.ERROR
                }
            }
        }
    }

    enum class GameConfigurationState {
        IDLE, CREATING_GAME, GAME_CREATED, ERROR
    }
}
