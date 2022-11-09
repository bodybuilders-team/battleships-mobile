package pt.isel.pdm.battleships.ui.screens.gameplay.boardSetup

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pt.isel.pdm.battleships.SessionManager
import pt.isel.pdm.battleships.domain.games.ship.Ship
import pt.isel.pdm.battleships.services.BattleshipsService
import pt.isel.pdm.battleships.services.games.dtos.GameDTO
import pt.isel.pdm.battleships.services.games.dtos.UndeployedFleetDTO
import pt.isel.pdm.battleships.services.utils.HTTPResult
import pt.isel.pdm.battleships.ui.screens.gameplay.boardSetup.BoardSetupViewModel.BoardSetupState.DEPLOYING_FLEET
import pt.isel.pdm.battleships.ui.screens.gameplay.boardSetup.BoardSetupViewModel.BoardSetupState.ERROR
import pt.isel.pdm.battleships.ui.screens.gameplay.boardSetup.BoardSetupViewModel.BoardSetupState.FLEET_DEPLOYED
import pt.isel.pdm.battleships.ui.screens.gameplay.boardSetup.BoardSetupViewModel.BoardSetupState.LOADING_GAME

/**
 * View model for the BoardSetupActivity.
 *
 * @param battleshipsService the service of the battleships application
 * @property sessionManager the manager used to handle the user session
 *
 * @property state the current state of the view model
 * @property game the game being played
 * @property errorMessage the error message to be displayed
 */
class BoardSetupViewModel(
    battleshipsService: BattleshipsService,
    val sessionManager: SessionManager
) : ViewModel() {

    private val gamesService = battleshipsService.gamesService
    private val playersService = battleshipsService.playersService

    var state by mutableStateOf(LOADING_GAME)
    var game by mutableStateOf<GameDTO?>(null)
    var errorMessage by mutableStateOf<String?>(null)

    /**
     * Loads the game.
     *
     * @param gameLink the link to the game
     */
    fun loadGame(gameLink: String) {
        if (state != LOADING_GAME) return

        viewModelScope.launch {
            val token = sessionManager.token ?: throw IllegalStateException("No token found")

            when (val res = gamesService.getGame(token, gameLink)) {
                is HTTPResult.Success -> {
                    game = res.data
                    state = DEPLOYING_FLEET
                }
                is HTTPResult.Failure -> {
                    errorMessage = res.error.message
                    state = ERROR
                }
            }
        }
    }

    /**
     * Deploys the fleet.
     *
     * @param deployFleetLink the link to the deploy fleet endpoint
     * @param fleet the fleet to be deployed
     */
    fun deployFleet(deployFleetLink: String, fleet: List<Ship>) {
        if (state != DEPLOYING_FLEET) return

        viewModelScope.launch {
            val token = sessionManager.token ?: throw IllegalStateException("No token found")
            val fleetDTOs = fleet.map(Ship::toUndeployedShipDTO)

            when (
                val res = playersService.deployFleet(
                    token = token,
                    deployLink = deployFleetLink,
                    fleet = UndeployedFleetDTO(fleetDTOs)
                )
            ) {
                is HTTPResult.Success -> {
                    state = FLEET_DEPLOYED
                }
                is HTTPResult.Failure -> {
                    errorMessage = res.error.message
                    state = ERROR
                }
            }
        }
    }

    /**
     * The state of the view model.
     *
     * @property LOADING_GAME the view model is loading the game
     * @property DEPLOYING_FLEET the view model is deploying the fleet
     * @property FLEET_DEPLOYED the fleet has been deployed
     * @property ERROR an error occurred
     */
    enum class BoardSetupState {
        LOADING_GAME,
        DEPLOYING_FLEET,
        FLEET_DEPLOYED,
        ERROR
    }
}
