package pt.isel.pdm.battleships.services.games.models.players.ship

data class GetFleetOutputModel(
    val ships: List<DeployedShipModel>
)
