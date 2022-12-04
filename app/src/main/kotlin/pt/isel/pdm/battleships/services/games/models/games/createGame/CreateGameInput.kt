package pt.isel.pdm.battleships.services.games.models.games.createGame

import pt.isel.pdm.battleships.services.games.models.games.GameConfigModel

/**
 * The Create Game Input.
 *
 * @property name the name of the game
 * @property config the game configuration
 */
data class CreateGameInput(
    val name: String,
    val config: GameConfigModel
)
