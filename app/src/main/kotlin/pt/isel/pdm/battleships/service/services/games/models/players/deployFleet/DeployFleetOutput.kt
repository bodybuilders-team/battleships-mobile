package pt.isel.pdm.battleships.service.services.games.models.players.deployFleet

import pt.isel.pdm.battleships.service.media.siren.SirenEntity

/**
 * The Deploy Fleet Output Model.
 *
 * @property successfullyDeployed true if the fleet was successfully deployed, false otherwise
 */
data class DeployFleetOutputModel(val successfullyDeployed: Boolean)

/**
 * The Deploy Fleet Output.
 */
typealias DeployFleetOutput = SirenEntity<DeployFleetOutputModel>
