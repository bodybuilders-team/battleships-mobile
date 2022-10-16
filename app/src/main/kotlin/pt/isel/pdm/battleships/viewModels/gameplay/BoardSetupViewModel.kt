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

enum class BoardSetupState {
    LOADING_GAME,
    PLACING_SHIPS,
    ERROR,
    READY
}

class BoardSetupViewModel(
    battleshipsService: BattleshipsService,
    val sessionManager: SessionManager
) : ViewModel() {
    private val gamesService = battleshipsService.gamesService
    private val playersService = battleshipsService.playersService

    var state by mutableStateOf(BoardSetupState.LOADING_GAME)
    var game by mutableStateOf<GameDTO?>(null)
    var errorMessage by mutableStateOf<String?>(null)

    fun loadGame(gameId: Int) {
        if (state != BoardSetupState.LOADING_GAME) {
            return
        }

        viewModelScope.launch {
            val token = sessionManager.token ?: throw IllegalStateException("No token found")

//            when (val res = gamesService.getGame(token, gameId)) {
//                is Result.Success -> {
//                    game = res.data
//                    state = BoardSetupState.PLACING_SHIPS
//                }
//                is Result.Failure -> {
//                    errorMessage = res.error.message
//                    state = BoardSetupState.ERROR
//                }
//            }
        }
    }

    fun deployFleet(fleet: List<Ship>) {
        if (state != BoardSetupState.PLACING_SHIPS) {
            return
        }

        viewModelScope.launch {
            val token = sessionManager.token ?: throw IllegalStateException("No token found")
            val currGame = game ?: throw IllegalStateException("No game found")

            val fleetDtos = fleet.map { it.toUndeployedShipDTO() }

//            when (val res = playersService.deployFleet(token, currGame.id, fleetDtos)) {
//                is Result.Success -> {
//                    state = BoardSetupState.READY
//                }
//                is Result.Failure -> {
//                    errorMessage = res.error.message
//                    state = BoardSetupState.ERROR
//                }
//            }
        }
    }
}
