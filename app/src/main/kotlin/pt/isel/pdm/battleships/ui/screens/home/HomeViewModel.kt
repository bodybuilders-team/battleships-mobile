package pt.isel.pdm.battleships.ui.screens.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield
import pt.isel.pdm.battleships.SessionManager
import pt.isel.pdm.battleships.services.BattleshipsService
import pt.isel.pdm.battleships.ui.screens.home.HomeViewModel.HomeLoadingState.LOADED
import pt.isel.pdm.battleships.ui.screens.home.HomeViewModel.HomeLoadingState.LOADING
import pt.isel.pdm.battleships.ui.screens.home.HomeViewModel.HomeLoadingState.NOT_LOADING
import pt.isel.pdm.battleships.ui.screens.home.HomeViewModel.HomeState.HOME_LOADED
import pt.isel.pdm.battleships.ui.screens.home.HomeViewModel.HomeState.IDLE
import pt.isel.pdm.battleships.ui.screens.home.HomeViewModel.HomeState.LINKS_LOADED
import pt.isel.pdm.battleships.ui.screens.home.HomeViewModel.HomeState.LOADING_HOME
import pt.isel.pdm.battleships.ui.screens.home.HomeViewModel.HomeState.LOADING_USER_HOME
import pt.isel.pdm.battleships.ui.screens.home.HomeViewModel.HomeState.USER_HOME_LINKS_LOADED
import pt.isel.pdm.battleships.ui.screens.home.HomeViewModel.HomeState.USER_HOME_LOADED
import pt.isel.pdm.battleships.ui.screens.shared.BattleshipsViewModel
import pt.isel.pdm.battleships.ui.utils.Event
import pt.isel.pdm.battleships.ui.utils.launchAndExecuteRequestRetrying
import pt.isel.pdm.battleships.ui.utils.navigation.Links

/**
 * View model for the [HomeActivity].
 *
 * @property battleshipsService the service of the battleships application
 *
 * @property events the events to be handled
 */
class HomeViewModel(
    battleshipsService: BattleshipsService,
    sessionManager: SessionManager
) : BattleshipsViewModel(battleshipsService, sessionManager) {

    data class GameplayScreenState(
        val state: HomeState = IDLE,
        val loadingState: HomeLoadingState = NOT_LOADING
    )

    private var _screenState by mutableStateOf(GameplayScreenState())
    val screenState: GameplayScreenState
        get() = _screenState

    /**
     * Loads the home page.
     */
    fun loadHome() {
        check(screenState.state == LINKS_LOADED) {
            "The view model is not in the links loaded state"
        }

        _screenState = _screenState.copy(state = LOADING_HOME)

        launchAndExecuteRequestRetrying(
            request = { battleshipsService.getHome() },
            events = _events,
            onSuccess = {
                _screenState = _screenState.copy(state = HOME_LOADED)
            }
        )
    }

    /**
     * Loads the user home links.
     */
    fun loadUserHome() {
        check(screenState.state == USER_HOME_LINKS_LOADED) {
            "The view model is not in the user home links loaded state"
        }

        _screenState = _screenState.copy(state = LOADING_USER_HOME)

        launchAndExecuteRequestRetrying(
            request = { battleshipsService.usersService.getUserHome() },
            events = _events,
            onSuccess = {
                _screenState = _screenState.copy(state = USER_HOME_LOADED)
            }
        )
    }

    /**
     * Navigates to the given activity.
     *
     * @param clazz the activity class to navigate to
     */
    fun <T> navigateTo(clazz: Class<T>) {
        _screenState = _screenState.copy(
            loadingState = LOADING
        )

        viewModelScope.launch {
            while (screenState.state !in listOf(HOME_LOADED, USER_HOME_LOADED))
                yield()
            _events.emit(HomeEvent.Navigate(clazz))
        }
    }

    /**
     * Navigates to the given activity.
     *
     * @param T the type of the activity to navigate to
     */
    inline fun <reified T> navigateTo() {
        navigateTo(T::class.java)
    }

    /**
     * Sets the loading state to [LOADED].
     */
    fun setLoadingStateToLoaded() {
        _screenState = _screenState.copy(loadingState = LOADED)
    }

    fun updateHomeLinks(links: Links) {
        super.updateLinks(links)
        _screenState = _screenState.copy(state = LINKS_LOADED)
    }

    fun updateUserHomeLinks(links: Links) {
        super.updateLinks(links)
        _screenState = _screenState.copy(state = USER_HOME_LINKS_LOADED)
    }

    /**
     * The state of the home screen.
     *
     * @property IDLE the home screen is idle
     * @property LOADING_HOME the home screen is loading
     * @property HOME_LOADED the home screen is loaded
     */
    enum class HomeState {
        IDLE,
        LINKS_LOADED,
        LOADING_HOME,
        HOME_LOADED,
        USER_HOME_LINKS_LOADED,
        LOADING_USER_HOME,
        USER_HOME_LOADED
    }

    /**
     * The state of the home screen loading.
     *
     * @property NOT_LOADING the home screen is idle
     * @property LOADING the home screen is loading
     * @property LOADED the home screen is not loading
     */
    enum class HomeLoadingState {
        NOT_LOADING,
        LOADING,
        LOADED
    }

    /**
     * The events of the home view model.
     */
    sealed class HomeEvent : Event {

        /**
         * A navigation event.
         *
         * @property clazz the activity class to navigate to
         */
        class Navigate(val clazz: Class<*>) : HomeEvent()
    }
}
