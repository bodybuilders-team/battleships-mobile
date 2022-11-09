package pt.isel.pdm.battleships.services.games.dtos

data class UndeployedShipDTO(
    val type: String,
    val position: String,
    val orientation: String
)
