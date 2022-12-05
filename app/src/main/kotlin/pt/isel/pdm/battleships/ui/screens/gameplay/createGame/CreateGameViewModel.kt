package pt.isel.pdm.battleships.ui.screens.gameplay.createGame

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay
import pt.isel.pdm.battleships.SessionManager
import pt.isel.pdm.battleships.domain.games.game.GameConfig
import pt.isel.pdm.battleships.service.BattleshipsService
import pt.isel.pdm.battleships.ui.screens.BattleshipsViewModel
import pt.isel.pdm.battleships.ui.screens.gameplay.createGame.CreateGameViewModel.CreateGameState.CREATING_GAME
import pt.isel.pdm.battleships.ui.screens.gameplay.createGame.CreateGameViewModel.CreateGameState.GAME_CREATED
import pt.isel.pdm.battleships.ui.screens.gameplay.createGame.CreateGameViewModel.CreateGameState.IDLE
import pt.isel.pdm.battleships.ui.screens.gameplay.createGame.CreateGameViewModel.CreateGameState.LINKS_LOADED
import pt.isel.pdm.battleships.ui.screens.gameplay.createGame.CreateGameViewModel.CreateGameState.OPPONENT_FOUND
import pt.isel.pdm.battleships.ui.screens.gameplay.createGame.CreateGameViewModel.CreateGameState.WAITING_FOR_OPPONENT
import pt.isel.pdm.battleships.ui.screens.shared.Event
import pt.isel.pdm.battleships.ui.screens.shared.executeRequestThrowing
import pt.isel.pdm.battleships.ui.screens.shared.launchAndExecuteRequestThrowing
import pt.isel.pdm.battleships.ui.screens.shared.navigation.Links

/**
 * View model for the [CreateGameActivity].
 *
 * @property state the current state of the view model
 */
class CreateGameViewModel(
    battleshipsService: BattleshipsService,
    sessionManager: SessionManager
) : BattleshipsViewModel(battleshipsService, sessionManager) {

    private var _state: CreateGameState by mutableStateOf(IDLE)
    val state
        get() = _state

    /**
     * Creates a new game.
     *
     * @param name the name of the game
     * @param config the game configuration
     */
    fun createGame(name: String, config: GameConfig) {
        check(state == LINKS_LOADED) { "The view model is not in the links loaded state" }

        _state = CREATING_GAME

        launchAndExecuteRequestThrowing(
            request = {
                battleshipsService.gamesService.createGame(
                    name = name,
                    config = config.toGameConfigModel()
                )
            },
            events = _events,
            onSuccess = {
                _state = GAME_CREATED
                _state = WAITING_FOR_OPPONENT

                while (true) {
                    val gameStateData = executeRequestThrowing(
                        request = { battleshipsService.gamesService.getGameState() },
                        events = _events
                    )

                    val gameStateProperties = gameStateData.properties
                        ?: throw IllegalStateException("Game state properties are null")

                    if (gameStateProperties.phase == WAITING_FOR_PLAYERS_PHASE) {
                        delay(POLLING_DELAY)
                    } else {
                        _events.emit(CreateGameEvent.NavigateToBoardSetup)
                        _state = OPPONENT_FOUND
                        break
                    }
                }
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
     * The create game state.
     *
     * @property CREATING_GAME creating a new game
     * @property GAME_CREATED the game was created
     */
    enum class CreateGameState {
        IDLE,
        LINKS_LOADED,
        CREATING_GAME,
        GAME_CREATED,
        WAITING_FOR_OPPONENT,
        OPPONENT_FOUND
    }

    /**
     * The events that can be emitted.
     */
    sealed class CreateGameEvent : Event {

        /**
         * A navigation event to the board setup screen.
         */
        object NavigateToBoardSetup : CreateGameEvent()
    }

    companion object {
        private const val WAITING_FOR_PLAYERS_PHASE = "WAITING_FOR_PLAYERS"
        private const val POLLING_DELAY = 500L
    }
}
