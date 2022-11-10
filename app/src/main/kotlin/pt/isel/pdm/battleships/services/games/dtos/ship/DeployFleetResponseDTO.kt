package pt.isel.pdm.battleships.services.games.dtos.ship

import pt.isel.pdm.battleships.services.utils.siren.SirenEntity

/**
 * Represents the data returned by the deploy fleet endpoint.
 *
 * @property successfullyDeployed true if the fleet was successfully deployed, false otherwise
 */
data class DeployFleetResponsePropertiesDTO(val successfullyDeployed: Boolean)

typealias DeployFleetResponseDTO = SirenEntity<DeployFleetResponsePropertiesDTO>
