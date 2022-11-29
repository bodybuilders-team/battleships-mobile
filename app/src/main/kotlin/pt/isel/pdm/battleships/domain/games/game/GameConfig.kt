package pt.isel.pdm.battleships.domain.games.game

import pt.isel.pdm.battleships.domain.games.ship.ShipType
import pt.isel.pdm.battleships.services.games.models.ShipTypeModel
import pt.isel.pdm.battleships.services.games.models.games.GameConfigModel

/**
 * A game configuration.
 *
 * @property gridSize The size of the grid.
 * @property shotsPerTurn The number of shots per turn.
 * @property maxTimePerRound The maximum time per shot.
 * @property maxTimeForLayoutPhase The maximum time for the layout phase.
 * @property ships The ships to be used in the game.
 */
data class GameConfig(
    val gridSize: Int,
    val shotsPerTurn: Int,
    val maxTimePerRound: Int,
    val maxTimeForLayoutPhase: Int,
    val ships: Map<ShipType, Int>
) {

    constructor(gameConfigModel: GameConfigModel) : this(
        gameConfigModel.gridSize,
        gameConfigModel.shotsPerRound,
        gameConfigModel.maxTimePerRound,
        gameConfigModel.maxTimeForLayoutPhase,
        gameConfigModel.shipTypes.associate {
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
        maxTimeForLayoutPhase = maxTimeForLayoutPhase,
        shotsPerRound = shotsPerTurn,
        maxTimePerRound = maxTimePerRound,
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
