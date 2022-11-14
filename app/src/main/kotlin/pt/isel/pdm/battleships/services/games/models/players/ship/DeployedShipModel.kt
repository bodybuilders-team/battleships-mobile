package pt.isel.pdm.battleships.services.games.models.players.ship

import pt.isel.pdm.battleships.services.games.models.CoordinateModel

data class DeployedShipModel(
    val type: String,
    val coordinate: CoordinateModel,
    val orientation: String,
    val lives: Int
)
