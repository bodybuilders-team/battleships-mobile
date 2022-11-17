package pt.isel.pdm.battleships.ui.screens.shared

import androidx.activity.ComponentActivity
import pt.isel.pdm.battleships.DependenciesContainer
import pt.isel.pdm.battleships.SessionManager
import pt.isel.pdm.battleships.services.BattleshipsService
import pt.isel.pdm.battleships.ui.utils.viewModelInit

open class BattleshipsActivity : ComponentActivity() {
    protected val dependenciesContainer by lazy {
        (application as DependenciesContainer)
    }

    // TODO See this crossinline later / Is function viewModelInit needed at all?
    protected inline fun <reified T : BattleshipsViewModel> getViewModel(
        crossinline viewModelConstructor:
            (battleshipsService: BattleshipsService, sessionManager: SessionManager) -> T
    ) = viewModelInit {
        viewModelConstructor(
            dependenciesContainer.battleshipsService,
            dependenciesContainer.sessionManager
        )
    }
}
