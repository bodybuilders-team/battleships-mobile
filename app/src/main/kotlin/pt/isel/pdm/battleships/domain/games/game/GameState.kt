package pt.isel.pdm.battleships.domain.games.game

import pt.isel.pdm.battleships.service.services.games.models.games.GameStateModel

/**
 * The Game State.
 *
 * @property phase the phase of the game
 * @property phaseEndTime the time when the current phase ends
 * @property round the round of the game
 * @property turn the turn of the game
 * @property winner the winner of the game
 */
data class GameState(
    val phase: String,
    val phaseEndTime: Long,
    val round: Int?,
    val turn: String?,
    val winner: String?
) {
    constructor(gameStateModel: GameStateModel) : this(
        phase = gameStateModel.phase,
        phaseEndTime = gameStateModel.phaseEndTime,
        round = gameStateModel.round,
        turn = gameStateModel.turn,
        winner = gameStateModel.winner
    )
}
