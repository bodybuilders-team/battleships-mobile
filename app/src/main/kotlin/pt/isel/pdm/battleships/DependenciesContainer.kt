package pt.isel.pdm.battleships

import com.google.gson.Gson
import pt.isel.pdm.battleships.services.BattleshipsService

/**
 * This interface is used to provide dependencies to the application.
 *
 * @property jsonEncoder the JSON formatter used to serialize/deserialize objects
 * @property sessionManager the manager used to handle the user session
 * @property battleshipsService the service used to handle the battleships game
 */
interface DependenciesContainer {
    val jsonEncoder: Gson
    val sessionManager: SessionManager
    val battleshipsService: BattleshipsService
}
