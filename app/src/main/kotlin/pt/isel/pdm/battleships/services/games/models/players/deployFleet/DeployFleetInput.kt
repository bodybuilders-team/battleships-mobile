package pt.isel.pdm.battleships.services.games.models.players.deployFleet

import pt.isel.pdm.battleships.services.games.models.players.ship.UndeployedShipModel

data class DeployFleetInput(val fleet: List<UndeployedShipModel>)
