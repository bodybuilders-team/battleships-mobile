package pt.isel.pdm.battleships.ui.screens.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield
import pt.isel.pdm.battleships.service.BattleshipsService
import pt.isel.pdm.battleships.session.SessionManager
import pt.isel.pdm.battleships.ui.screens.BattleshipsViewModel
import pt.isel.pdm.battleships.ui.screens.authentication.AuthenticationViewModel.AuthenticationState.LINKS_LOADED
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
import pt.isel.pdm.battleships.ui.screens.shared.Event
import pt.isel.pdm.battleships.ui.screens.shared.launchAndExecuteRequest
import pt.isel.pdm.battleships.ui.screens.shared.launchAndExecuteRequestThrowing
import pt.isel.pdm.battleships.ui.screens.shared.navigation.Links
import pt.isel.pdm.battleships.ui.screens.shared.navigation.Rels

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
    private var _state: HomeState by mutableStateOf(IDLE)
    private var _isLoggedIn: Boolean by mutableStateOf(false)

    val loadingState: HomeLoadingState
        get() = _loadingState

    val state
        get() = _state

    val isLoggedIn
        get() = _isLoggedIn

    val username
        get() = sessionManager.username

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

                if (sessionManager.isLoggedIn()) {
                    val newLinks = getLinks().links
                        .toMutableMap()
                        .also { links -> links[Rels.USER_HOME] = sessionManager.userHomeLink!! }

                    updateUserHomeLinks(Links(links = newLinks))
                    loadUserHome()
                }
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
                _isLoggedIn = true
            }
        )
    }

    /**
     * Logs out the user.
     */
    fun logout() {
        check(sessionManager.isLoggedIn()) { "The user is not logged in." }

        val refreshToken = sessionManager.refreshToken
        sessionManager.clearSession()
        _isLoggedIn = false

        refreshToken ?: return

        launchAndExecuteRequest(
            request = { battleshipsService.usersService.logout(refreshToken) },
            events = _events,
            onSuccess = {},
            retryOnApiResultFailure = { false }
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
     */
    fun updateHomeLinks() {
        super.updateLinks(Links(emptyMap()))
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
     * @property IDLE the initial state
     * @property LINKS_LOADED the state when the links are loaded
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
