package pt.isel.pdm.battleships.viewModels

import android.content.res.AssetManager
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import pt.isel.pdm.battleships.SessionManager
import pt.isel.pdm.battleships.services.BattleshipsService

const val DEFAULT_GAME_CONFIG_FILE_PATH = "defaultGameConfig.json"

/**
 * Represents the ViewModel for the Battleships game.
 *
 * @property battleshipsService the service used to handle the battleships game
 * @property sessionManager the session manager used to handle the user session
 * @property assetManager the asset manager used to access the game configuration file
 * @property jsonFormatter the JSON formatter used to parse the game configuration file
 */
class BattleshipsViewModel(
    private val battleshipsService: BattleshipsService,
    private val sessionManager: SessionManager,
    private val assetManager: AssetManager,
    private val jsonFormatter: Gson
) : ViewModel() {
//    fun test() {
//        viewModelScope.launch {
//            while (sessionManager.token == null) {
//                delay(10)
//            }
//            try {
//                val token = sessionManager.token!!
//                val mres = battleshipsService.gamesService.matchmake(token, gameConfigDTO)
//                mres as MatchmakeResult.Success
//                val gres = battleshipsService.gamesService.getGameById(token, mres.dto.gameId)
//                print(gres)
//                1
//            } catch (e: Exception) {
//                Log.v("BattleshipsViewModelTest", "Error: ${e.message}")
//                e.printStackTrace()
//            }
//        }
//    }
}
