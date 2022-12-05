package pt.isel.pdm.battleships.ui.screens

import androidx.activity.ComponentActivity
import pt.isel.pdm.battleships.DependenciesContainer
import pt.isel.pdm.battleships.SessionManager
import pt.isel.pdm.battleships.service.BattleshipsService
import pt.isel.pdm.battleships.ui.screens.shared.viewModelInit

/**
 * Activity for the [BattleshipsScreen].
 * The base activity for all battleships activities.
 */
open class BattleshipsActivity : ComponentActivity() {

    protected val dependenciesContainer by lazy {
        (application as DependenciesContainer)
    }

    /**
     * Gets an initialized [BattleshipsViewModel].
     *
     * @param T the type of the [BattleshipsViewModel] to be initialized
     * @param constructor the constructor for the view model
     *
     * @return the view model
     */
    protected inline fun <reified T : BattleshipsViewModel> getViewModel(
        crossinline constructor: (
            battleshipsService: BattleshipsService,
            sessionManager: SessionManager
        ) -> T
    ) = viewModelInit {
        constructor(
            dependenciesContainer.battleshipsService,
            dependenciesContainer.sessionManager
        )
    }
}
