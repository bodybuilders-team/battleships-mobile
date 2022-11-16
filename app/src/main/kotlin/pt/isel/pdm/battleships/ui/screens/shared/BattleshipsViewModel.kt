package pt.isel.pdm.battleships.ui.screens.shared

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import pt.isel.pdm.battleships.SessionManager
import pt.isel.pdm.battleships.services.BattleshipsService
import pt.isel.pdm.battleships.services.LinkBattleshipsService
import pt.isel.pdm.battleships.ui.screens.shared.BattleshipsViewModel.BattleshipsState.Companion.LINKS_LOADED
import pt.isel.pdm.battleships.ui.utils.Event
import pt.isel.pdm.battleships.ui.utils.navigation.Links

abstract class BattleshipsViewModel(
    private val _battleshipsService: BattleshipsService,
    protected val sessionManager: SessionManager
) : ViewModel() {

    @Suppress("PropertyName")
    protected val _events = MutableSharedFlow<Event>()
    val events: SharedFlow<Event> = _events

    protected lateinit var battleshipsService: LinkBattleshipsService

    @Suppress("PropertyName")
    protected open var _state: BattleshipsState by mutableStateOf(BattleshipsState.IDLE)
    val state: BattleshipsState
        get() = _state

    /**
     * Updates the links, updating [battleshipsService] with a new instance of
     * [LinkBattleshipsService] and setting the state to [LINKS_LOADED].
     *
     * @param links the links to update
     */
    fun updateLinks(links: Links) {
        battleshipsService = LinkBattleshipsService(
            links = links.links,
            battleshipsService = _battleshipsService,
            sessionManager = sessionManager
        )
        _state = LINKS_LOADED
    }

    /**
     * Gets the links.
     *
     * @return the links
     */
    fun getLinks() = Links(battleshipsService.links)

    interface BattleshipsState {
        companion object : BattleshipsStateCompanion()
    }

    @Suppress("PropertyName", "ObjectPropertyName")
    open class BattleshipsStateCompanion {
        val IDLE = _IDLE
        val LINKS_LOADED = _LINKS_LOADED

        companion object {
            private val _IDLE = object : BattleshipsState {}
            private val _LINKS_LOADED = object : BattleshipsState {}
        }
    }
}
