package pt.isel.pdm.battleships.services.games.models.players.deployFleet

import pt.isel.pdm.battleships.services.utils.siren.SirenEntity

/**
 * The data returned by the deploy fleet endpoint.
 *
 * @property successfullyDeployed true if the fleet was successfully deployed, false otherwise
 */
data class DeployFleetOutputModel(val successfullyDeployed: Boolean)

typealias DeployFleetOutput = SirenEntity<DeployFleetOutputModel>
