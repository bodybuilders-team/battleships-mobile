package pt.isel.pdm.battleships.services.games.dtos.ship

data class UndeployedShipDTO(
    val type: String,
    val position: String,
    val orientation: String
)
