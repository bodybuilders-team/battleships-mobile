package pt.isel.pdm.battleships.ui.screens.gameplay.gameConfiguration

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import pt.isel.pdm.battleships.SessionManager
import pt.isel.pdm.battleships.domain.games.game.GameConfig
import pt.isel.pdm.battleships.services.BattleshipsService
import pt.isel.pdm.battleships.ui.screens.BattleshipsViewModel
import pt.isel.pdm.battleships.ui.screens.gameplay.gameConfiguration.GameConfigurationViewModel.GameConfigurationState.CREATING_GAME
import pt.isel.pdm.battleships.ui.screens.gameplay.gameConfiguration.GameConfigurationViewModel.GameConfigurationState.GAME_CREATED
import pt.isel.pdm.battleships.ui.screens.gameplay.gameConfiguration.GameConfigurationViewModel.GameConfigurationState.IDLE
import pt.isel.pdm.battleships.ui.screens.gameplay.gameConfiguration.GameConfigurationViewModel.GameConfigurationState.LINKS_LOADED
import pt.isel.pdm.battleships.ui.utils.Event
import pt.isel.pdm.battleships.ui.utils.launchAndExecuteRequestRetrying
import pt.isel.pdm.battleships.ui.utils.navigation.Links

/**
 * View model for the [GameConfigurationActivity].
 *
 * @property state the current state of the view model
 */
class GameConfigurationViewModel(
    battleshipsService: BattleshipsService,
    sessionManager: SessionManager
) : BattleshipsViewModel(battleshipsService, sessionManager) {

    private var _state: GameConfigurationState by mutableStateOf(IDLE)
    val state
        get() = _state

    /**
     * Creates a new game.
     *
     * @param gameConfig the game configuration
     */
    fun createGame(gameConfig: GameConfig) {
        check(state == LINKS_LOADED) { "The view model is not in the links loaded state" }

        _state = CREATING_GAME

        launchAndExecuteRequestRetrying(
            request = {
                battleshipsService.gamesService.createGame(
                    gameConfig = gameConfig.toGameConfigModel()
                )
            },
            events = _events,
            onSuccess = {
                _state = GAME_CREATED
                _events.emit(GameConfigurationEvent.NavigateToBoardSetup)
            }
        )
    }

    /**
     * Updates the links.
     *
     * @param links the links to update
     */
    override fun updateLinks(links: Links) {
        super.updateLinks(links)
        _state = LINKS_LOADED
    }

    /**
     * The game configuration state.
     *
     * @property CREATING_GAME creating a new game
     * @property GAME_CREATED the game was created
     */
    enum class GameConfigurationState {
        IDLE,
        LINKS_LOADED,
        CREATING_GAME,
        GAME_CREATED
    }

    /**
     * The events that can be emitted.
     */
    sealed class GameConfigurationEvent : Event {

        /**
         * A navigation event to the board setup screen.
         */
        object NavigateToBoardSetup : GameConfigurationEvent()
    }
}
