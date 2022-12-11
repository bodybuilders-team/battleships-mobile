package pt.isel.pdm.battleships.domain.games.game

import pt.isel.pdm.battleships.domain.games.ship.ShipType
import pt.isel.pdm.battleships.service.services.games.models.ShipTypeModel
import pt.isel.pdm.battleships.service.services.games.models.games.GameConfigModel

/**
 * A game configuration.
 *
 * @property gridSize the size of the grid
 * @property shotsPerRound the number of shots per round
 * @property maxTimePerRound the maximum time per shot
 * @property maxTimeForLayoutPhase the maximum time for the layout phase
 * @property ships the ships to be used in the game
 */
data class GameConfig(
    val gridSize: Int,
    val shotsPerRound: Int,
    val maxTimePerRound: Int,
    val maxTimeForLayoutPhase: Int,
    val ships: Map<ShipType, Int>
) {

    constructor(gameConfigModel: GameConfigModel) : this(
        gridSize = gameConfigModel.gridSize,
        shotsPerRound = gameConfigModel.shotsPerRound,
        maxTimePerRound = gameConfigModel.maxTimePerRound,
        maxTimeForLayoutPhase = gameConfigModel.maxTimeForLayoutPhase,
        ships = gameConfigModel.shipTypes.associate {
            ShipType(size = it.size, shipName = it.shipName) to it.quantity
        }
    )

    /**
     * Converts the GameConfig to a GameConfigModel.
     *
     * @return the GameConfigModel
     */
    fun toGameConfigModel(): GameConfigModel = GameConfigModel(
        gridSize = gridSize,
        shotsPerRound = shotsPerRound,
        maxTimePerRound = maxTimePerRound,
        maxTimeForLayoutPhase = maxTimeForLayoutPhase,
        shipTypes = ships.map { (shipType, quantity) ->
            ShipTypeModel(
                shipName = shipType.shipName,
                size = shipType.size,
                quantity = quantity,
                points = shipType.points
            )
        }
    )
}
