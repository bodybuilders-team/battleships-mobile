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
import pt.isel.pdm.battleships.ui.screens.gameplay.gameplayMenu.GameplayMenuViewModel.GameplayMenuLoadingState.NOT_LOADING
import pt.isel.pdm.battleships.ui.screens.gameplay.gameplayMenu.GameplayMenuViewModel.GameplayMenuState.IDLE
import pt.isel.pdm.battleships.ui.screens.gameplay.gameplayMenu.GameplayMenuViewModel.GameplayMenuState.LOADED
import pt.isel.pdm.battleships.ui.utils.Event

/**
 * View model for the [GameplayMenuActivity].
 *
 * @property usersService the service that handles the users
 *
 * @property loadingState the current loading state of the view model
 * @property state the current state of the view model
 * @property links the links to the next screens
 * @property events the events that can be emitted by the view model
 */
class GameplayMenuViewModel(
    private val usersService: UsersService
) : ViewModel() {
    var loadingState: GameplayMenuLoadingState by mutableStateOf(NOT_LOADING)
    var state by mutableStateOf(IDLE)
    var links: Map<String, String> = emptyMap()

    private val _events = MutableSharedFlow<Event>()
    val events: SharedFlow<Event> = _events

    /**
     * Emits a navigation event to the activity with the given [links].
     *
     * @param clazz the class of the activity to navigate to
     * @param linkRels the relation links to pass to the activity
     */
    fun <T> navigateTo(clazz: Class<T>, linkRels: Set<String>? = null) {
        loadingState = GameplayMenuLoadingState.LOADING
        viewModelScope.launch {
            while (state != LOADED)
                yield()
            _events.emit(GameplayMenuEvent.Navigate(clazz, linkRels))
        }
    }

    /**
     * Emits a navigation event to the activity with the given [links].
     *
     * @param T the type of the activity to navigate to
     * @param linkRels the relation links to pass to the activity
     */
    inline fun <reified T> navigateTo(linkRels: Set<String>? = null) {
        navigateTo(T::class.java, linkRels)
    }

    /**
     * The state of the gameplay menu screen.
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
     * The state of the gameplay menu screen loading.
     *
     * @property NOT_LOADING the gameplay menu screen is not loading
     * @property LOADING the gameplay menu screen is loading
     */
    enum class GameplayMenuLoadingState {
        LOADING,
        NOT_LOADING
    }

    /**
     * The events that can be emitted by the gameplay menu view model.
     */
    sealed class GameplayMenuEvent : Event {

        /**
         * A navigation event.
         *
         * @property clazz the class of the activity to navigate to
         * @property linkRels the relation links to pass to the activity
         */
        class Navigate(val clazz: Class<*>, val linkRels: Set<String>? = null) : GameplayMenuEvent()
    }
}
