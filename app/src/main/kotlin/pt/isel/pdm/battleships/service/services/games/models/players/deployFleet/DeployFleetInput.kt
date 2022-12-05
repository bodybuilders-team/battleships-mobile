package pt.isel.pdm.battleships.service.services.games.models.players.deployFleet

import pt.isel.pdm.battleships.service.services.games.models.players.ship.UndeployedShipModel

/**
 * The Deploy Fleet Input.
 *
 * @param fleet the list of ships to deploy
 */
data class DeployFleetInput(val fleet: List<UndeployedShipModel>)
