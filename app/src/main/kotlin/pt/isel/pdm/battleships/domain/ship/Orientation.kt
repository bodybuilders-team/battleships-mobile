package pt.isel.pdm.battleships.domain.ship

/**
 * Represents the ship's orientation.
 */
enum class Orientation : java.io.Serializable {
    VERTICAL,
    HORIZONTAL;

    /**
     * Checks if the orientation is vertical.
     *
     * @return true if the orientation is vertical, false otherwise.
     */
    fun isVertical() = this == VERTICAL

    /**
     * Checks if the orientation is horizontal.
     *
     * @return true if the orientation is horizontal, false otherwise.
     */
    fun isHorizontal() = this == HORIZONTAL

    /**
     * Returns the opposite orientation.
     *
     * @return the opposite orientation.
     */
    fun opposite() = if (this == VERTICAL) HORIZONTAL else VERTICAL
}
