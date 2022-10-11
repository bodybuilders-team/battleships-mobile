package pt.isel.pdm.battleships.domain.game

import pt.isel.pdm.battleships.domain.ship.ShipType
import pt.isel.pdm.battleships.services.games.dtos.GameConfigDTO
import java.io.Serializable

/**
 * Represents a game configuration.
 *
 * @property gridSize The size of the grid.
 * @property shotsPerTurn The number of shots per turn.
 * @property maxTimePerShot The maximum time per shot.
 * @property maxTimeForLayoutPhase The maximum time for the layout phase.
 * @property ships The ships to be used in the game.
 */
data class GameConfig(
    val gridSize: Int,
    val shotsPerTurn: Int,
    val maxTimePerShot: Int,
    val maxTimeForLayoutPhase: Int,
    val ships: List<ShipType>
) : Serializable {

    /**
     * Converts this game configuration to a DTO.
     *
     * @return the DTO
     */
    fun toDTO(): GameConfigDTO = GameConfigDTO(
        gridSize,
        maxTimeForLayoutPhase,
        shotsPerTurn,
        maxTimePerShot,
        ships.map { it.toDTO() }
    )
}
