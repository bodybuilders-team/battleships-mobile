package pt.isel.pdm.battleships.services.games.dtos.ship

import pt.isel.pdm.battleships.services.games.dtos.CoordinateDTO

data class DeployedShipDTO(
    val type: String,
    val coordinate: CoordinateDTO,
    val orientation: String,
    val lives: Int
)
