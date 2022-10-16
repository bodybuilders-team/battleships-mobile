package pt.isel.pdm.battleships.services.players.dtos

/**
 * Represents the data returned by the deploy fleet endpoint.
 *
 * @property successfullyDeployed true if the fleet was successfully deployed, false otherwise
 */
data class DeployFleetResponseDTO(val successfullyDeployed: Boolean)
