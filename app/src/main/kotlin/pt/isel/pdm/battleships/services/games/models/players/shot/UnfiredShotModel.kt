package pt.isel.pdm.battleships.services.games.models.players.shot

import pt.isel.pdm.battleships.services.games.models.CoordinateModel

/**
 * The Unfired Shot Model.
 *
 * @property coordinate the coordinate of the shot
 */
data class UnfiredShotModel(
    val coordinate: CoordinateModel
)
