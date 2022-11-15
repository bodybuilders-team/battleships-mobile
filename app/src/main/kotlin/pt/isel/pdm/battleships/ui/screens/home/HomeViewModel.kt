package pt.isel.pdm.battleships.ui.screens.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield
import pt.isel.pdm.battleships.services.BattleshipsService
import pt.isel.pdm.battleships.ui.screens.home.HomeViewModel.HomeLoadingState.LOADING
import pt.isel.pdm.battleships.ui.screens.home.HomeViewModel.HomeLoadingState.NOT_LOADING
import pt.isel.pdm.battleships.ui.screens.home.HomeViewModel.HomeState.HOME_LOADED
import pt.isel.pdm.battleships.ui.screens.home.HomeViewModel.HomeState.IDLE
import pt.isel.pdm.battleships.ui.screens.home.HomeViewModel.HomeState.LOADING_HOME
import pt.isel.pdm.battleships.ui.screens.home.HomeViewModel.HomeState.LOADING_USER_HOME
import pt.isel.pdm.battleships.ui.screens.home.HomeViewModel.HomeState.USER_HOME_LOADED
import pt.isel.pdm.battleships.ui.utils.Event
import pt.isel.pdm.battleships.ui.utils.launchAndExecuteRequestRetrying

/**
 * View model for the [HomeActivity].
 *
 * @property battleshipsService the service of the battleships application
 *
 * @property loadingState the current loading state
 * @property state the current state of the view model
 * @property links the actions to be displayed
 * @property events the events to be handled
 */
class HomeViewModel(
    private val battleshipsService: BattleshipsService
) : ViewModel() {

    var loadingState: HomeLoadingState by mutableStateOf(NOT_LOADING)
    var state by mutableStateOf(IDLE)
    var links: Map<String, String> = emptyMap()

    private val _events = MutableSharedFlow<Event>()
    val events: SharedFlow<Event> = _events

    /**
     * Loads the home page.
     */
    fun loadHome() {
        check(state == IDLE) { "The view model is not in the idle state" }

        state = LOADING_HOME

        launchAndExecuteRequestRetrying(
            request = { battleshipsService.getHome() },
            events = _events,
            onSuccess = { homeData ->
                val resActions = homeData.actions ?: emptyList()
                links = resActions.associate { Pair(it.name, it.href.path) }
                state = HOME_LOADED
            }
        )
    }

    /**
     * Loads the user home links.
     */
    fun loadUserHome(userHomeLink: String) {
        check(state == HOME_LOADED) { "The view model is not in the home loaded state" }

        state = LOADING_USER_HOME

        launchAndExecuteRequestRetrying(
            request = { battleshipsService.usersService.getUserHome(userHomeLink = userHomeLink) },
            events = _events,
            onSuccess = { userHomeData ->
                val resActions = userHomeData.actions ?: emptyList()
                links = resActions.associate { Pair(it.name, it.href.path) }
                state = USER_HOME_LOADED
            }
        )
    }

    /**
     * Navigates to the given activity with the specified links.
     *
     * @param clazz the activity class to navigate to
     * @param linkRels the link rels to be used to get the link
     */
    fun <T> navigateTo(clazz: Class<T>, linkRels: Set<String>? = null) {
        loadingState = LOADING
        viewModelScope.launch {
            while (state != HOME_LOADED)
                yield()
            _events.emit(HomeEvent.Navigate(clazz, linkRels))
        }
    }

    /**
     * Navigates to the given activity with the specified links.
     *
     * @param T the type of the activity to navigate to
     * @param linkRels the link rels to be used to get the link
     */
    inline fun <reified T> navigateTo(linkRels: Set<String>? = null) {
        navigateTo(T::class.java, linkRels)
        // TODO: Pass links instead of linkRels
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
        LOADING_HOME,
        HOME_LOADED,
        LOADING_USER_HOME,
        USER_HOME_LOADED
    }

    /**
     * The state of the home screen loading.
     *
     * @property NOT_LOADING the home screen is not loading
     * @property LOADING the home screen is loading
     */
    enum class HomeLoadingState {
        LOADING,
        NOT_LOADING
    }

    /**
     * The events of the home view model.
     */
    sealed class HomeEvent : Event {

        /**
         * A navigation event.
         *
         * @property clazz the activity class to navigate to
         * @property linkRels the link rels to be used to get the link
         */
        class Navigate(val clazz: Class<*>, val linkRels: Set<String>? = null) : HomeEvent()
    }
}
