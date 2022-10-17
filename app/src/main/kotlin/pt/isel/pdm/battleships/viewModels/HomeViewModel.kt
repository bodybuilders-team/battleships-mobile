package pt.isel.pdm.battleships.viewModels

import android.content.res.AssetManager
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield
import pt.isel.pdm.battleships.SessionManager
import pt.isel.pdm.battleships.services.BattleshipsService
import pt.isel.pdm.battleships.services.utils.Result
import pt.isel.pdm.battleships.services.utils.siren.Action
import pt.isel.pdm.battleships.viewModels.HomeViewModel.HomeState.ERROR
import pt.isel.pdm.battleships.viewModels.HomeViewModel.HomeState.IDLE
import pt.isel.pdm.battleships.viewModels.HomeViewModel.HomeState.LOADED
import pt.isel.pdm.battleships.viewModels.HomeViewModel.HomeState.LOADING
import pt.isel.pdm.battleships.viewModels.HomeViewModel.LoadingState.LOADING_ABOUT
import pt.isel.pdm.battleships.viewModels.HomeViewModel.LoadingState.LOADING_GAMEPLAY_MENU
import pt.isel.pdm.battleships.viewModels.HomeViewModel.LoadingState.LOADING_LOGIN
import pt.isel.pdm.battleships.viewModels.HomeViewModel.LoadingState.LOADING_RANKING
import pt.isel.pdm.battleships.viewModels.HomeViewModel.LoadingState.LOADING_REGISTER
import pt.isel.pdm.battleships.viewModels.HomeViewModel.LoadingState.NOT_LOADING
import java.io.IOException

/**
 * Represents the ViewModel for the Battleships game.
 *
 * @property battleshipsService the service used to handle the battleships game
 * @property sessionManager the session manager used to handle the user session
 * @property assetManager the asset manager used to access the game configuration file
 * @property jsonFormatter the JSON formatter used to parse the game configuration file
 *
 * @property loadingState the current loading state
 * @property state the current state of the home page
 * @property errorMessage the error message to be displayed
 * @property actions the actions to be displayed
 */
class HomeViewModel(
    private val battleshipsService: BattleshipsService,
    private val sessionManager: SessionManager,
    private val assetManager: AssetManager,
    private val jsonFormatter: Gson
) : ViewModel() {

    var loadingState: LoadingState by mutableStateOf(NOT_LOADING)
    var state by mutableStateOf(IDLE)
    private var errorMessage by mutableStateOf<String?>(null)
    var actions: Map<String, Action> = emptyMap()

    /**
     * Loads the home page.
     */
    fun loadHome() {
        if (state != IDLE) return

        state = LOADING

        viewModelScope.launch {
            val res = try {
                battleshipsService.getHome()
            } catch (e: IOException) {
                errorMessage = "Could not connect to the server."
                state = ERROR
                return@launch
            }

            when (res) {
                is Result.Success -> {
                    val resActions = res.data.actions ?: emptyList()
                    actions = resActions.associateBy { it.name }
                    state = LOADED
                }
                is Result.Failure -> {
                    errorMessage = res.error.message
                    state = ERROR
                }
            }
        }
    }

    /**
     * Executes the given callback when the home page is loaded.
     *
     * @param callback the callback to be executed
     */
    fun onHomeLoaded(callback: () -> Unit) {
        if (state != LOADING) return callback()

        viewModelScope.launch {
            while (state == LOADING) {
                yield()
            }

            callback()
        }
    }

    /**
     * Represents the state of the home screen.
     *
     * @property IDLE the home screen is idle
     * @property LOADING the home screen is loading
     * @property LOADED the home screen is loaded
     * @property ERROR the home screen is in error state
     */
    enum class HomeState {
        IDLE,
        LOADING,
        LOADED,
        ERROR
    }

    /**
     * Represents the state of the home screen loading.
     *
     * @property NOT_LOADING the home screen is not loading
     * @property LOADING_GAMEPLAY_MENU the home screen is loading the gameplay menu
     * @property LOADING_LOGIN the home screen is loading the login
     * @property LOADING_REGISTER the home screen is loading the register
     * @property LOADING_RANKING the home screen is loading the ranking
     * @property LOADING_ABOUT the home screen is loading the about
     */
    enum class LoadingState {
        LOADING_GAMEPLAY_MENU,
        LOADING_LOGIN,
        LOADING_REGISTER,
        LOADING_RANKING,
        LOADING_ABOUT,
        NOT_LOADING
    }
}
