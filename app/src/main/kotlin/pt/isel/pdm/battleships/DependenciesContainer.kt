package pt.isel.pdm.battleships

import pt.isel.pdm.battleships.service.BattleshipsService

/**
 * This interface is used to provide dependencies to the application.
 *
 * @property battleshipsService The service used to handle the battleships game.
 */
interface DependenciesContainer {
    val sessionManager: SessionManager
    val battleshipsService: BattleshipsService
}
