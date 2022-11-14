package pt.isel.pdm.battleships.services.games.models.players.ship

/**
 * The Get Fleet Output Model.
 *
 * @param ships the list of ships
 */
data class GetFleetOutputModel(
    val ships: List<DeployedShipModel>
)
