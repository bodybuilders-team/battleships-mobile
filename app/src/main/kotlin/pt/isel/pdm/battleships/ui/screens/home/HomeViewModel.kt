package pt.isel.pdm.battleships.ui.screens.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield
import pt.isel.pdm.battleships.SessionManager
import pt.isel.pdm.battleships.services.BattleshipsService
import pt.isel.pdm.battleships.ui.screens.BattleshipsViewModel
import pt.isel.pdm.battleships.ui.screens.home.HomeViewModel.HomeLoadingState.LOADED
import pt.isel.pdm.battleships.ui.screens.home.HomeViewModel.HomeLoadingState.LOADING
import pt.isel.pdm.battleships.ui.screens.home.HomeViewModel.HomeLoadingState.NOT_LOADING
import pt.isel.pdm.battleships.ui.screens.home.HomeViewModel.HomeState.HOME_LINKS_LOADED
import pt.isel.pdm.battleships.ui.screens.home.HomeViewModel.HomeState.HOME_LOADED
import pt.isel.pdm.battleships.ui.screens.home.HomeViewModel.HomeState.IDLE
import pt.isel.pdm.battleships.ui.screens.home.HomeViewModel.HomeState.LOADING_HOME
import pt.isel.pdm.battleships.ui.screens.home.HomeViewModel.HomeState.LOADING_USER_HOME
import pt.isel.pdm.battleships.ui.screens.home.HomeViewModel.HomeState.USER_HOME_LINKS_LOADED
import pt.isel.pdm.battleships.ui.screens.home.HomeViewModel.HomeState.USER_HOME_LOADED
import pt.isel.pdm.battleships.ui.utils.Event
import pt.isel.pdm.battleships.ui.utils.launchAndExecuteRequestThrowing
import pt.isel.pdm.battleships.ui.utils.navigation.Links

/**
 * View model for the [HomeActivity].
 *
 * @property battleshipsService the service used to handle the battleships game
 * @property sessionManager the manager used to handle the user session
 *
 * @property loadingState the current loading state of the view model
 * @property state the current state of the view model
 */
class HomeViewModel(
    battleshipsService: BattleshipsService,
    sessionManager: SessionManager
) : BattleshipsViewModel(battleshipsService, sessionManager) {

    private var _loadingState by mutableStateOf(NOT_LOADING)
    val loadingState: HomeLoadingState
        get() = _loadingState

    private var _state: HomeState by mutableStateOf(IDLE)
    val state
        get() = _state

    /**
     * Loads the home page.
     */
    fun loadHome() {
        check(state == HOME_LINKS_LOADED) { "The view model is not in the links loaded state." }

        _state = LOADING_HOME

        launchAndExecuteRequestThrowing(
            request = { battleshipsService.getHome() },
            events = _events,
            onSuccess = {
                _state = HOME_LOADED
            }
        )
    }

    /**
     * Loads the user home links.
     */
    fun loadUserHome() {
        check(state == USER_HOME_LINKS_LOADED) {
            "The view model is not in the user home links loaded state."
        }

        _state = LOADING_USER_HOME

        launchAndExecuteRequestThrowing(
            request = { battleshipsService.usersService.getUserHome() },
            events = _events,
            onSuccess = {
                _state = USER_HOME_LOADED
            }
        )
    }

    /**
     * Logs out the user.
     */
    fun logout() {
        check(sessionManager.isLoggedIn()) { "The user is not logged in." }

        launchAndExecuteRequestThrowing(
            request = { battleshipsService.usersService.logout(sessionManager.refreshToken!!) },
            events = _events,
            onSuccess = { sessionManager.clearSession() }
        )
    }

    /**
     * Navigates to the given activity.
     *
     * @param clazz the activity class to navigate to
     */
    fun <T> navigateTo(clazz: Class<T>) {
        _loadingState = LOADING

        viewModelScope.launch {
            while (state !in listOf(HOME_LOADED, USER_HOME_LOADED))
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
        _loadingState = LOADED
    }

    /**
     * Updates the home links.
     *
     * @param links the new home links
     */
    fun updateHomeLinks(links: Links) {
        super.updateLinks(links)
        _state = HOME_LINKS_LOADED
    }

    /**
     * Updates the user home links.
     *
     * @param links the new user home links
     */
    fun updateUserHomeLinks(links: Links) {
        super.updateLinks(links)
        _state = USER_HOME_LINKS_LOADED
    }

    /**
     * The state of the [HomeViewModel].
     *
     * @property IDLE the view model is idle
     * @property HOME_LINKS_LOADED the home links are loaded
     * @property LOADING_HOME the home screen is loading
     * @property HOME_LOADED the home screen is loaded
     * @property USER_HOME_LINKS_LOADED the user home links are loaded
     * @property LOADING_USER_HOME the user home screen is loading
     * @property USER_HOME_LOADED the user home screen is loaded
     */
    enum class HomeState {
        IDLE,
        HOME_LINKS_LOADED,
        LOADING_HOME,
        HOME_LOADED,
        USER_HOME_LINKS_LOADED,
        LOADING_USER_HOME,
        USER_HOME_LOADED
    }

    /**
     * The loading state of the [HomeViewModel].
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
     * The events of the [HomeViewModel].
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
