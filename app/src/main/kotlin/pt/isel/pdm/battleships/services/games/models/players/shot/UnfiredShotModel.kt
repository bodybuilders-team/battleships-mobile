package pt.isel.pdm.battleships.services.games.models.players.shot

import pt.isel.pdm.battleships.services.games.models.CoordinateModel

/**
 * An Unfired Shot DTO.
 *
 * @property coordinate the coordinate of the shot
 */
data class UnfiredShotModel(
    val coordinate: CoordinateModel
)
