package pt.isel.pdm.battleships.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pt.isel.pdm.battleships.SessionManager
import pt.isel.pdm.battleships.domain.game.GameConfig
import pt.isel.pdm.battleships.services.games.GamesService

/**
 * View model for the search game activity.
 *
 * @property sessionManager the session manager used to handle the user session
 * @property gamesService the game service
 */
class SearchGameViewModel(
    private val sessionManager: SessionManager,
    private val gamesService: GamesService
) : ViewModel() {

    fun matchmake(gameConfig: GameConfig) {
        viewModelScope.launch {
            val res = gamesService.matchmake(sessionManager.token!!, gameConfig.toDTO())
        }
    }
}
