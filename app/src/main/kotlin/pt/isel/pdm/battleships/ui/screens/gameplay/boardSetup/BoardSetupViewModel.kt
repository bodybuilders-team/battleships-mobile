package pt.isel.pdm.battleships.ui.screens.gameplay.boardSetup

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay
import pt.isel.pdm.battleships.SessionManager
import pt.isel.pdm.battleships.domain.games.ship.Ship
import pt.isel.pdm.battleships.domain.games.ship.ShipType
import pt.isel.pdm.battleships.services.BattleshipsService
import pt.isel.pdm.battleships.services.games.models.players.deployFleet.DeployFleetInput
import pt.isel.pdm.battleships.ui.screens.gameplay.boardSetup.BoardSetupViewModel.BoardSetupState.DEPLOYING_FLEET
import pt.isel.pdm.battleships.ui.screens.gameplay.boardSetup.BoardSetupViewModel.BoardSetupState.FINISHED
import pt.isel.pdm.battleships.ui.screens.gameplay.boardSetup.BoardSetupViewModel.BoardSetupState.FLEET_DEPLOYED
import pt.isel.pdm.battleships.ui.screens.gameplay.boardSetup.BoardSetupViewModel.BoardSetupState.GAME_LOADED
import pt.isel.pdm.battleships.ui.screens.gameplay.boardSetup.BoardSetupViewModel.BoardSetupState.LINKS_LOADED
import pt.isel.pdm.battleships.ui.screens.gameplay.boardSetup.BoardSetupViewModel.BoardSetupState.LOADING_GAME
import pt.isel.pdm.battleships.ui.screens.gameplay.boardSetup.BoardSetupViewModel.BoardSetupState.WAITING_FOR_OPPONENT
import pt.isel.pdm.battleships.ui.screens.shared.BattleshipsViewModel
import pt.isel.pdm.battleships.ui.utils.Event
import pt.isel.pdm.battleships.ui.utils.executeRequestRetrying
import pt.isel.pdm.battleships.ui.utils.launchAndExecuteRequestRetrying

/**
 * View model for the [BoardSetupActivity].
 *
 * @param battleshipsService the service of the battleships application
 * @property sessionManager the manager used to handle the user session
 *
 * @property events the events that occurred in the view model
 */
class BoardSetupViewModel(
    battleshipsService: BattleshipsService,
    sessionManager: SessionManager
) : BattleshipsViewModel(battleshipsService, sessionManager) {

    data class BoardSetupScreenState(
        val gridSize: Int? = null,
        val ships: List<ShipType>? = null
    )

    private var _screenState by mutableStateOf(BoardSetupScreenState())
    val screenState: BoardSetupScreenState
        get() = _screenState

    /**
     * Loads the game.
     */
    fun loadGame() {
        check(state == LINKS_LOADED) { "The game is not in the links loaded state" }

        _state = LOADING_GAME

        launchAndExecuteRequestRetrying(
            request = { battleshipsService.gamesService.getGame() },
            events = _events,
            onSuccess = { getGameData ->
                val properties = getGameData.properties
                    ?: throw IllegalStateException("No game properties found")

                val shipTypes = properties.config.shipTypes

                _screenState = _screenState.copy(
                    gridSize = properties.config.gridSize,
                    ships = shipTypes.flatMap { shipType ->
                        List(shipType.quantity) {
                            ShipType(
                                size = shipType.size,
                                shipName = shipType.shipName
                            )
                        }
                    }
                )
                _state = GAME_LOADED
            }
        )
    }

    /**
     * Deploys the fleet.
     *
     * @param fleet the fleet to be deployed
     */
    fun deployFleet(fleet: List<Ship>) {
        check(state == GAME_LOADED) { "The game is not loaded" }

        _state = DEPLOYING_FLEET

        launchAndExecuteRequestRetrying(
            request = {
                battleshipsService.playersService.deployFleet(
                    fleet = DeployFleetInput(fleet = fleet.map(Ship::toUndeployedShipModel))
                )
            },
            events = _events,
            onSuccess = {
                _state = FLEET_DEPLOYED
                waitForOpponent()
            }
        )
    }

    /**
     * Waits for the opponent to deploy their fleet.
     */
    private suspend fun waitForOpponent() {
        check(state == FLEET_DEPLOYED) { "The game is not in the fleet deployed state" }

        _state = WAITING_FOR_OPPONENT

        while (true) {
            val gameStateData = executeRequestRetrying(
                request = { battleshipsService.gamesService.getGameState() },
                events = _events
            )

            val properties = gameStateData.properties
                ?: throw IllegalStateException("Game state properties are null")

            if (properties.phase == DEPLOYING_FLEETS_PHASE) {
                delay(POLLING_DELAY)
            } else {
                _state = FINISHED
                _events.emit(BoardSetupEvent.NavigateToGameplay)
                break
            }
        }
    }

    /**
     * The state of the view model.
     *
     * @property LOADING_GAME the view model is loading the game
     * @property DEPLOYING_FLEET the view model is deploying the fleet
     * @property WAITING_FOR_OPPONENT WAITING_FOR_OPPONENT
     */
    object BoardSetupState : BattleshipsState, BattleshipsStateCompanion() {
        val LOADING_GAME = object : BattleshipsState {}
        val GAME_LOADED = object : BattleshipsState {}
        val DEPLOYING_FLEET = object : BattleshipsState {}
        val FLEET_DEPLOYED = object : BattleshipsState {}
        val WAITING_FOR_OPPONENT = object : BattleshipsState {}
        val FINISHED = object : BattleshipsState {}
    }

    /**
     * The events that can be emitted.
     */
    sealed class BoardSetupEvent : Event {

        /**
         * The event of navigating to the gameplay screen.
         */
        object NavigateToGameplay : BoardSetupEvent()
    }

    companion object {
        private const val DEPLOYING_FLEETS_PHASE = "DEPLOYING_FLEETS"
        private const val POLLING_DELAY = 1000L
    }
}
