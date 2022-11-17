package pt.isel.pdm.battleships.ui.screens.gameplay.matchmake

import android.content.res.AssetManager
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import pt.isel.pdm.battleships.SessionManager
import pt.isel.pdm.battleships.services.BattleshipsService
import pt.isel.pdm.battleships.services.games.models.games.GameConfigModel
import pt.isel.pdm.battleships.ui.screens.gameplay.matchmake.MatchmakeViewModel.MatchmakeState.IDLE
import pt.isel.pdm.battleships.ui.screens.gameplay.matchmake.MatchmakeViewModel.MatchmakeState.LINKS_LOADED
import pt.isel.pdm.battleships.ui.screens.gameplay.matchmake.MatchmakeViewModel.MatchmakeState.MATCHMADE
import pt.isel.pdm.battleships.ui.screens.gameplay.matchmake.MatchmakeViewModel.MatchmakeState.MATCHMAKING
import pt.isel.pdm.battleships.ui.screens.shared.BattleshipsViewModel
import pt.isel.pdm.battleships.ui.utils.Event
import pt.isel.pdm.battleships.ui.utils.executeRequestRetrying
import pt.isel.pdm.battleships.ui.utils.navigation.Links

/**
 * View model for the [MatchmakeActivity].
 *
 * @property battleshipsService the service of the battleships application
 * @property sessionManager the manager used to handle the user session
 * @param jsonEncoder the JSON formatter
 * @param assetManager the asset manager
 *
 * @property state the current state of the view model
 * @property events the events that can be emitted by the view model
 */
class MatchmakeViewModel(
    battleshipsService: BattleshipsService,
    sessionManager: SessionManager,
    jsonEncoder: Gson,
    assetManager: AssetManager
) : BattleshipsViewModel(battleshipsService, sessionManager) {

    // TODO should this be attached to viewModel or application data?
    private val gameConfigModel = jsonEncoder.fromJson<GameConfigModel>(
        JsonReader(assetManager.open(DEFAULT_GAME_CONFIG_FILE_PATH).reader()),
        GameConfigModel::class.java
    )

    private var _state: MatchmakeState by mutableStateOf(IDLE)
    val state
        get() = _state

    /**
     * Matchmakes a game with the default configuration.
     */
    fun matchmake() {
        check(state == LINKS_LOADED) { "The view model is not in the links loaded state" }

        _state = MATCHMAKING

        viewModelScope.launch {
            delay(ANIMATION_DELAY)

            val matchmakeData = executeRequestRetrying(
                request = {
                    battleshipsService.gamesService.matchmake(gameConfig = gameConfigModel)
                },
                events = _events
            )

            val properties = matchmakeData.properties
                ?: throw IllegalStateException("Game properties are null")

            if (!properties.wasCreated) {
                _events.emit(MatchmakeEvent.NavigateToBoardSetup)
                _state = MATCHMADE
                return@launch
            }

            while (true) {
                val gameStateData = executeRequestRetrying(
                    request = { battleshipsService.gamesService.getGameState() },
                    events = _events
                )

                val gameStateProperties = gameStateData.properties
                    ?: throw IllegalStateException("Game state properties are null")

                if (gameStateProperties.phase == WAITING_FOR_PLAYERS_PHASE) {
                    delay(POLLING_DELAY)
                } else {
                    _events.emit(MatchmakeEvent.NavigateToBoardSetup)
                    _state = MATCHMADE
                    break
                }
            }
        }
    }

    override fun updateLinks(links: Links) {
        super.updateLinks(links)
        _state = LINKS_LOADED
    }

    /**
     * The matchmake operation state.
     *
     * @property MATCHMAKING the matchmaking is in progress
     * @property MATCHMADE the matchmake was successful
     */
    enum class MatchmakeState {
        IDLE,
        LINKS_LOADED,
        MATCHMAKING,
        MATCHMADE
    }

    /**
     * The events that can be emitted.
     */
    sealed class MatchmakeEvent : Event {

        /**
         * Navigation event to the board setup screen.
         */
        object NavigateToBoardSetup : MatchmakeEvent()
    }

    companion object {
        private const val DEFAULT_GAME_CONFIG_FILE_PATH = "defaultGameConfig.json"
        private const val WAITING_FOR_PLAYERS_PHASE = "WAITING_FOR_PLAYERS"
        private const val POLLING_DELAY = 500L
        private const val ANIMATION_DELAY = 1500L
    }
}
