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
    val ships: List<ShipType>
) {

    constructor(dto: GameConfigModel) : this(
        dto.gridSize,
        dto.shotsPerRound,
        dto.maxTimePerRound,
        dto.maxTimeForLayoutPhase,
        dto.shipTypes.flatMap { shipType ->
            List(shipType.quantity) {
                ShipType(
                    size = shipType.size,
                    shipName = shipType.shipName
                )
            }
        }
    )

    /**
     * Converts this game configuration to a DTO.
     *
     * @return the GameConfigDTO
     */
    fun toGameConfigDTO(): GameConfigModel = GameConfigModel(
        gridSize,
        maxTimeForLayoutPhase,
        shotsPerTurn,
        maxTimePerRound,
        ships.distinct().map { ship ->
            ShipTypeModel(
                shipName = ship.shipName,
                size = ship.size,
                quantity = ships.count { it == ship },
                points = ship.points
            )
        }
    )
}
