package pt.isel.pdm.battleships.services.games.models.games.createGame

import pt.isel.pdm.battleships.services.games.models.games.GameConfigModel

/**
 * The Create Game Input.
 *
 * @property name the name of the game
 * @property gameConfig the game configuration
 */
data class CreateGameInput(
    val name: String,
    val gameConfig: GameConfigModel
)
