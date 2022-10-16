package pt.isel.pdm.battleships.viewModels

import android.content.res.AssetManager
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import java.io.IOException
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield
import pt.isel.pdm.battleships.SessionManager
import pt.isel.pdm.battleships.services.BattleshipsService
import pt.isel.pdm.battleships.services.utils.Result
import pt.isel.pdm.battleships.services.utils.siren.Action

enum class HomeState {
    IDLE,
    LOADING,
    ERROR,
    LOADED
}

enum class RefreshingState {
    REFRESHING_GAMEPLAY_MENU,
    REFRESHING_LOGIN,
    REFRESHING_REGISTER,
    REFRESHING_RANKING,
    REFRESHING_ABOUT,
    NOT_REFRESHING
}

/**
 * Represents the ViewModel for the Battleships game.
 *
 * @property battleshipsService the service used to handle the battleships game
 * @property sessionManager the session manager used to handle the user session
 * @property assetManager the asset manager used to access the game configuration file
 * @property jsonFormatter the JSON formatter used to parse the game configuration file
 */
class HomeViewModel(
    private val battleshipsService: BattleshipsService,
    private val sessionManager: SessionManager,
    private val assetManager: AssetManager,
    private val jsonFormatter: Gson
) : ViewModel() {
    var refreshingState: RefreshingState by mutableStateOf(RefreshingState.NOT_REFRESHING)
    var state by mutableStateOf(HomeState.IDLE)
    var errorMessage by mutableStateOf<String?>(null)
    var actions: Map<String, Action> = emptyMap()

    fun loadHome() {
        if (state != HomeState.IDLE) return

        state = HomeState.LOADING

        viewModelScope.launch {
            val res = try {
                battleshipsService.getHome()
            } catch (e: IOException) {
                errorMessage = "Could not connect to the server."
                state = HomeState.ERROR
                return@launch
            }

            when (res) {
                is Result.Success -> {
                    val resActions = res.data.actions ?: emptyList()
                    actions = resActions.associateBy { it.name }

                    state = HomeState.LOADED
                }
                is Result.Failure -> {
                    errorMessage = res.error.message
                    state = HomeState.ERROR
                }
            }
        }
    }

    fun onHomeLoaded(callback: () -> Unit) {
        if (state != HomeState.LOADING) {
            return callback()
        }

        viewModelScope.launch {
            while (state == HomeState.LOADING) {
                yield()
            }

            callback()
        }
    }
}
