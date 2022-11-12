package pt.isel.pdm.battleships.ui.screens.gameplay.gameplayMenu

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield
import pt.isel.pdm.battleships.services.users.UsersService
import pt.isel.pdm.battleships.services.utils.APIResult
import pt.isel.pdm.battleships.ui.screens.gameplay.gameplayMenu.GameplayMenuViewModel.GameplayMenuState.IDLE
import pt.isel.pdm.battleships.ui.screens.gameplay.gameplayMenu.GameplayMenuViewModel.GameplayMenuState.LOADED
import pt.isel.pdm.battleships.ui.screens.gameplay.gameplayMenu.GameplayMenuViewModel.GameplayMenuState.LOADING
import pt.isel.pdm.battleships.ui.screens.gameplay.gameplayMenu.GameplayMenuViewModel.LoadingState.NOT_LOADING
import pt.isel.pdm.battleships.ui.utils.HTTPResult
import pt.isel.pdm.battleships.ui.utils.tryExecuteHttpRequest

class GameplayMenuViewModel(
    private val usersService: UsersService
) : ViewModel() {
    var loadingState: LoadingState by mutableStateOf(NOT_LOADING)
    var state by mutableStateOf(IDLE)
    var links: Map<String, String> = emptyMap()

    private val _events = MutableSharedFlow<Event>()
    val events: SharedFlow<Event> = _events

    /**
     * Loads the user home links.
     */
    fun loadUserHome(userHomeLink: String) {
        if (state != IDLE) return

        state = LOADING

        viewModelScope.launch {
            val httpRes = tryExecuteHttpRequest {
                usersService.getUserHome(userHomeLink)
            }

            val res = when (httpRes) {
                is HTTPResult.Success -> httpRes.data
                is HTTPResult.Failure -> {
                    _events.emit(Event.Error(httpRes.error))
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
                    _events.emit(Event.Error(res.error.title))
                    state = IDLE
                }
            }
        }
    }

    /**
     * Emits a navigation event to the activity with the given [links].
     */
    fun <T> navigateTo(clazz: Class<T>, linkRels: Set<String>? = null) {
        loadingState = LoadingState.LOADING
        viewModelScope.launch {
            while (state != LOADED)
                yield()
            _events.emit(Event.Navigate(clazz, linkRels))
        }
    }

    /**
     * Emits a navigation event to the activity with the given [links].
     */
    inline fun <reified T> navigateTo(linkRels: Set<String>? = null) {
        navigateTo(T::class.java, linkRels)
    }

    /**
     * Represents the state of the gameplay menu screen.
     *
     * @property IDLE the gameplay menu screen is idle
     * @property LOADING the gameplay menu screen is loading
     * @property LOADED the gameplay menu screen is loaded
     */
    enum class GameplayMenuState {
        IDLE,
        LOADING,
        LOADED
    }

    /**
     * Represents the state of the gameplay menu screen loading.
     *
     * @property NOT_LOADING the gameplay menu screen is not loading
     * @property LOADING the gameplay menu screen is loading
     */
    enum class LoadingState {
        LOADING,
        NOT_LOADING
    }

    /**
     * Represents the events that can be emitted by the gameplay menu screen.
     */
    sealed class Event {
        class Error(val message: String) : Event()
        class Navigate(val clazz: Class<*>, val linkRels: Set<String>? = null) : Event()
    }
}
