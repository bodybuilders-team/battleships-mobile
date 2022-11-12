package pt.isel.pdm.battleships.domain.games.game

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import pt.isel.pdm.battleships.domain.games.ship.ShipType
import pt.isel.pdm.battleships.services.games.dtos.GameConfigDTO

/**
 * Represents a game configuration.
 *
 * @property gridSize The size of the grid.
 * @property shotsPerTurn The number of shots per turn.
 * @property maxTimePerRound The maximum time per shot.
 * @property maxTimeForLayoutPhase The maximum time for the layout phase.
 * @property ships The ships to be used in the game.
 */
@Parcelize
data class GameConfig(
    val gridSize: Int,
    val shotsPerTurn: Int,
    val maxTimePerRound: Int,
    val maxTimeForLayoutPhase: Int,
    val ships: List<ShipType>
) : Parcelable {

    constructor(dto: GameConfigDTO) : this(
        dto.gridSize,
        dto.shotsPerRound,
        dto.maxTimePerRound,
        dto.maxTimeForLayoutPhase,
        dto.shipTypes.map { ShipType.fromShipTypeDTO(it) }
    )

    /**
     * Converts this game configuration to a DTO.
     *
     * @return the DTO
     */
    fun toDTO(): GameConfigDTO = GameConfigDTO(
        gridSize,
        maxTimeForLayoutPhase,
        shotsPerTurn,
        maxTimePerRound,
        ships.map { it.toShipTypeDTO() }
    )
}
