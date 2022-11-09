package pt.isel.pdm.battleships.viewModels.gameplay

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pt.isel.pdm.battleships.SessionManager
import pt.isel.pdm.battleships.domain.ship.Ship
import pt.isel.pdm.battleships.services.BattleshipsService
import pt.isel.pdm.battleships.services.games.dtos.GameDTO
import pt.isel.pdm.battleships.services.players.dtos.UndeployedFleetDTO
import pt.isel.pdm.battleships.services.utils.HTTPResult

class BoardSetupViewModel(
    battleshipsService: BattleshipsService,
    val sessionManager: SessionManager
) : ViewModel() {
    private val gamesService = battleshipsService.gamesService
    private val playersService = battleshipsService.playersService

    var state by mutableStateOf(BoardSetupState.LOADING_GAME)
    var game by mutableStateOf<GameDTO?>(null)
    var errorMessage by mutableStateOf<String?>(null)

    fun loadGame(gameLink: String) {
        if (state != BoardSetupState.LOADING_GAME) {
            return
        }

        viewModelScope.launch {
            val token = sessionManager.token ?: throw IllegalStateException("No token found")

            when (val res = gamesService.getGame(token, gameLink)) {
                is HTTPResult.Success -> {
                    game = res.data
                    state = BoardSetupState.DEPLOYING_FLEET
                }
                is HTTPResult.Failure -> {
                    errorMessage = res.error.message
                    state = BoardSetupState.ERROR
                }
            }
        }
    }

    fun deployFleet(deployFleetLink: String, fleet: List<Ship>) {
        if (state != BoardSetupState.DEPLOYING_FLEET) {
            return
        }

        viewModelScope.launch {
            val token = sessionManager.token ?: throw IllegalStateException("No token found")

            val fleetDTOs = fleet.map { it.toUndeployedShipDTO() }

            when (
                val res =
                    playersService.deployFleet(
                        token,
                        deployFleetLink,
                        UndeployedFleetDTO(fleetDTOs)
                    )
            ) {
                is HTTPResult.Success -> {
                    state = BoardSetupState.FLEET_DEPLOYED
                }
                is HTTPResult.Failure -> {
                    errorMessage = res.error.message
                    state = BoardSetupState.ERROR
                }
            }
        }
    }

    enum class BoardSetupState {
        LOADING_GAME,
        DEPLOYING_FLEET,
        FLEET_DEPLOYED,
        ERROR
    }
}
