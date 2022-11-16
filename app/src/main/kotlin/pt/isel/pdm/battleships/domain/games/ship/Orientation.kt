package pt.isel.pdm.battleships.domain.games.ship

import pt.isel.pdm.battleships.domain.games.ship.Orientation.HORIZONTAL
import pt.isel.pdm.battleships.domain.games.ship.Orientation.VERTICAL

/**
 * The ship's orientation.
 *
 * @property VERTICAL the vertical orientation
 * @property HORIZONTAL the horizontal orientation
 */
enum class Orientation {
    VERTICAL,
    HORIZONTAL;

    /**
     * Checks if the orientation is vertical.
     *
     * @return true if the orientation is vertical, false otherwise
     */
    fun isVertical() = this == VERTICAL

    /**
     * Checks if the orientation is horizontal.
     *
     * @return true if the orientation is horizontal, false otherwise
     */
    fun isHorizontal() = this == HORIZONTAL

    /**
     * Returns the opposite orientation.
     *
     * @return the opposite orientation
     */
    fun opposite() = if (this == VERTICAL) HORIZONTAL else VERTICAL
}
