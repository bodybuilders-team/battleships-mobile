package pt.isel.pdm.battleships.viewModels

import android.content.res.AssetManager
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import pt.isel.pdm.battleships.SessionManager
import pt.isel.pdm.battleships.service.BattleshipsService

const val DEFAULT_GAME_CONFIG_FILE_PATH = "defaultGameConfig.json"

/**
 * Represents the ViewModel for the Battleships game.
 *
 * @property battleshipsService The service used to handle the battleships game.
 */
class BattleshipsViewModel(
    private val battleshipsService: BattleshipsService,
    private val sessionManager: SessionManager,
    private val assetManager: AssetManager,
    private val jsonFormatter: Gson
) : ViewModel() {
//    fun test() {
//        viewModelScope.launch {
//            val gameConfigDTO = jsonFormatter.fromJson<GameConfigDTO>(
//                JsonReader(assetManager.open(DEFAULT_GAME_CONFIG_FILE_PATH).reader()),
//                GameConfigDTO::class.java
//            )
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
