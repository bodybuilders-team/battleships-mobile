package pt.isel.pdm.battleships.ui.screens

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import pt.isel.pdm.battleships.SessionManager
import pt.isel.pdm.battleships.service.BattleshipsService
import pt.isel.pdm.battleships.service.LinkBattleshipsService
import pt.isel.pdm.battleships.ui.screens.shared.Event
import pt.isel.pdm.battleships.ui.screens.shared.navigation.Links

/**
 * View model for the [BattleshipsActivity].
 * Base class for all view models that are used in the application.
 *
 * @property battleshipsService the service used to handle the battleships game
 * @property sessionManager the manager used to handle the user session
 * @property events the events that occurred in the view model
 */
abstract class BattleshipsViewModel(
    private val _battleshipsService: BattleshipsService,
    protected val sessionManager: SessionManager
) : ViewModel() {

    @Suppress("PropertyName")
    protected val _events = MutableSharedFlow<Event>()
    val events: SharedFlow<Event> = _events

    protected lateinit var battleshipsService: LinkBattleshipsService

    /**
     * Updates the links, updating [battleshipsService] with a new instance of
     * [LinkBattleshipsService].
     *
     * @param links the links to update
     */
    open fun updateLinks(links: Links) {
        battleshipsService = LinkBattleshipsService(
            links = links.links,
            battleshipsService = _battleshipsService,
            sessionManager = sessionManager
        )
    }

    /**
     * Gets the links.
     *
     * @return the links
     */
    fun getLinks() = Links(battleshipsService.links)
}
