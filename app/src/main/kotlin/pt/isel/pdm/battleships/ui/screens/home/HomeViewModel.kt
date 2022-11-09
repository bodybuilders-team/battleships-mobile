package pt.isel.pdm.battleships.ui.screens.home

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
import pt.isel.pdm.battleships.services.utils.HTTPResult
import pt.isel.pdm.battleships.services.utils.siren.Action
import pt.isel.pdm.battleships.ui.screens.home.HomeViewModel.HomeState.ERROR
import pt.isel.pdm.battleships.ui.screens.home.HomeViewModel.HomeState.IDLE
import pt.isel.pdm.battleships.ui.screens.home.HomeViewModel.HomeState.LOADED
import pt.isel.pdm.battleships.ui.screens.home.HomeViewModel.HomeState.LOADING
import pt.isel.pdm.battleships.ui.screens.home.HomeViewModel.LoadingState.LOADING_ABOUT
import pt.isel.pdm.battleships.ui.screens.home.HomeViewModel.LoadingState.LOADING_GAMEPLAY_MENU
import pt.isel.pdm.battleships.ui.screens.home.HomeViewModel.LoadingState.LOADING_LOGIN
import pt.isel.pdm.battleships.ui.screens.home.HomeViewModel.LoadingState.LOADING_RANKING
import pt.isel.pdm.battleships.ui.screens.home.HomeViewModel.LoadingState.LOADING_REGISTER
import pt.isel.pdm.battleships.ui.screens.home.HomeViewModel.LoadingState.NOT_LOADING
import java.io.IOException

/**
 * View model for the HomeActivity.
 *
 * @property battleshipsService the service of the battleships application
 * @property sessionManager the manager used to handle the user session
 * @property jsonEncoder the JSON formatter
 * @property assetManager the asset manager
 *
 * @property loadingState the current loading state
 * @property state the current state of the view model
 * @property errorMessage the error message to be displayed
 * @property actions the actions to be displayed
 */
class HomeViewModel(
    private val battleshipsService: BattleshipsService,
    private val sessionManager: SessionManager,
    private val jsonEncoder: Gson,
    private val assetManager: AssetManager
) : ViewModel() {

    var loadingState: LoadingState by mutableStateOf(NOT_LOADING)
    var state by mutableStateOf(IDLE)
    var errorMessage by mutableStateOf<String?>(null)
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
                is HTTPResult.Success -> {
                    val resActions = res.data.actions ?: emptyList()
                    actions = resActions.associateBy(Action::name)
                    state = LOADED
                }
                is HTTPResult.Failure -> {
                    errorMessage = "Could not load the home page."
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
        if (state == LOADED) return callback()

        viewModelScope.launch {
            while (state != LOADED) {
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
