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
import pt.isel.pdm.battleships.services.utils.APIResult
import pt.isel.pdm.battleships.ui.screens.home.HomeViewModel.HomeLoadingState.NOT_LOADING
import pt.isel.pdm.battleships.ui.screens.home.HomeViewModel.HomeState.IDLE
import pt.isel.pdm.battleships.ui.screens.home.HomeViewModel.HomeState.LOADED
import pt.isel.pdm.battleships.ui.screens.home.HomeViewModel.HomeState.LOADING
import pt.isel.pdm.battleships.ui.utils.HTTPResult
import pt.isel.pdm.battleships.ui.utils.tryExecuteHttpRequest

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

    private val _events = MutableSharedFlow<HomeEvent>()
    val events: SharedFlow<HomeEvent> = _events

    /**
     * Loads the home page.
     */
    fun loadHome() {
        if (state != IDLE) return

        state = HomeState.LOADING

        viewModelScope.launch {
            val httpRes = tryExecuteHttpRequest {
                battleshipsService.getHome()
            }

            val res = when (httpRes) {
                is HTTPResult.Success -> httpRes.data
                is HTTPResult.Failure -> {
                    _events.emit(HomeEvent.Error(httpRes.error))
                    state = IDLE
                    return@launch
                }
            }

            when (res) {
                is APIResult.Success -> {
                    val resActions = res.data.actions ?: emptyList()
                    links = resActions.associate { Pair(it.name, it.href.path) }
                    state = LOADED
                }
                is APIResult.Failure -> {
                    _events.emit(HomeEvent.Error(res.error.title))
                    state = IDLE
                }
            }
        }
    }

    /**
     * Navigates to the given activity with the specified links.
     *
     * @param clazz the activity class to navigate to
     * @param linkRels the link rels to be used to get the link
     */
    fun <T> navigateTo(clazz: Class<T>, linkRels: Set<String>? = null) {
        loadingState = HomeLoadingState.LOADING
        viewModelScope.launch {
            while (state != LOADED)
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
     * Represents the state of the home screen.
     *
     * @property IDLE the home screen is idle
     * @property LOADING the home screen is loading
     * @property LOADED the home screen is loaded
     */
    enum class HomeState {
        IDLE,
        LOADING,
        LOADED
    }

    /**
     * Represents the state of the home screen loading.
     *
     * @property NOT_LOADING the home screen is not loading
     * @property LOADING the home screen is loading
     */
    enum class HomeLoadingState {
        LOADING,
        NOT_LOADING
    }

    /**
     * Represents the events of the home view model.
     */
    sealed class HomeEvent {

        /**
         * Represents an error event.
         *
         * @property message the error message
         */
        class Error(val message: String) : HomeEvent()

        /**
         * Represents a navigation event.
         *
         * @property clazz the activity class to navigate to
         * @property linkRels the link rels to be used to get the link
         */
        class Navigate(val clazz: Class<*>, val linkRels: Set<String>? = null) : HomeEvent()
    }
}
