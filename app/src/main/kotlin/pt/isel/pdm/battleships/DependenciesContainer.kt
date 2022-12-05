package pt.isel.pdm.battleships

import com.google.gson.Gson
import pt.isel.pdm.battleships.service.BattleshipsService
import pt.isel.pdm.battleships.session.SessionManager

/**
 * Provides the dependencies of the application.
 *
 * @property jsonEncoder the JSON encoder used to serialize/deserialize objects
 * @property sessionManager the manager used to handle the user session
 * @property battleshipsService the service used to handle the battleships game
 */
interface DependenciesContainer {
    val jsonEncoder: Gson
    val sessionManager: SessionManager
    val battleshipsService: BattleshipsService
}
