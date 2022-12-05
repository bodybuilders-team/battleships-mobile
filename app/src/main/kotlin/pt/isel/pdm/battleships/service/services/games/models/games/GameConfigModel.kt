package pt.isel.pdm.battleships.service.services.games.models.games

import pt.isel.pdm.battleships.service.services.games.models.ShipTypeModel

/**
 * The Game Config Model.
 *
 * @property gridSize the size of the grid
 * @property maxTimePerRound the maximum time per shot
 * @property shotsPerRound the number of shots per round
 * @property maxTimeForLayoutPhase the maximum time for the layout phase
 * @property shipTypes the ship types allowed in the game
 */
data class GameConfigModel(
    val gridSize: Int,
    val maxTimeForLayoutPhase: Int,
    val shotsPerRound: Int,
    val maxTimePerRound: Int,
    val shipTypes: List<ShipTypeModel>
)
