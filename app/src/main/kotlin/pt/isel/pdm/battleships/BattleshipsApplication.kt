package pt.isel.pdm.battleships

import android.app.Application
import pt.isel.pdm.battleships.service.BattleshipsService

/**
 * Represents the Battleships application.
 *
 * @property battleshipsService The service used to handle the battleships game.
 */
class BattleshipsApplication : DependenciesContainer, Application() {

    override val battleshipsService = BattleshipsService()

    companion object {
        const val TAG = "BattleshipsApp"
    }
}
