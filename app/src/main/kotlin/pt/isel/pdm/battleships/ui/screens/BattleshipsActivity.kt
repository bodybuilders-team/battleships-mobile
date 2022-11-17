package pt.isel.pdm.battleships.ui.screens

import androidx.activity.ComponentActivity
import pt.isel.pdm.battleships.DependenciesContainer
import pt.isel.pdm.battleships.SessionManager
import pt.isel.pdm.battleships.services.BattleshipsService
import pt.isel.pdm.battleships.ui.utils.viewModelInit

/**
 * Activity for the [BattleshipsScreen].
 * The base activity for all battleships activities.
 */
open class BattleshipsActivity : ComponentActivity() {

    protected val dependenciesContainer by lazy {
        (application as DependenciesContainer)
    }

    /**
     * Gets the view model for this activity.
     *
     * @param viewModelConstructor the constructor for the view model
     * @return the view model
     */ // TODO See this crossinline later / Is function viewModelInit needed at all?
    protected inline fun <reified T : BattleshipsViewModel> getViewModel(
        crossinline viewModelConstructor: (
            battleshipsService: BattleshipsService,
            sessionManager: SessionManager
        ) -> T
    ) = viewModelInit {
        viewModelConstructor(
            dependenciesContainer.battleshipsService,
            dependenciesContainer.sessionManager
        )
    }
}
