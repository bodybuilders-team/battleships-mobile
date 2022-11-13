package pt.isel.pdm.battleships.services.games.dtos.ship

import pt.isel.pdm.battleships.services.utils.siren.SirenEntity

data class GetMyFleetResponsePropertiesDTO(
    val ships: List<DeployedShipDTO>
)

typealias GetMyFleetResponseDTO = SirenEntity<GetMyFleetResponsePropertiesDTO>
