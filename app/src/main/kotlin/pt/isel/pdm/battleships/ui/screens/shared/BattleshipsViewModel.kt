package pt.isel.pdm.battleships.ui.screens.shared

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import pt.isel.pdm.battleships.SessionManager
import pt.isel.pdm.battleships.services.BattleshipsService
import pt.isel.pdm.battleships.services.LinkBattleshipsService
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
    fun getLinks() =
        Links(battleshipsService.links)
}
