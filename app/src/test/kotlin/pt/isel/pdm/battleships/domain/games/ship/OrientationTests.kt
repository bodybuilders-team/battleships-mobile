package pt.isel.pdm.battleships.domain.games.ship

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class OrientationTests {

    @Test
    fun `Orientation is vertical when it is vertical`() {
        assertTrue { Orientation.VERTICAL.isVertical() }
    }

    @Test
    fun `Orientation is not vertical when it is horizontal`() {
        assertFalse { Orientation.HORIZONTAL.isVertical() }
    }

    @Test
    fun `Orientation is horizontal when it is horizontal`() {
        assertTrue { Orientation.HORIZONTAL.isHorizontal() }
    }

    @Test
    fun `Orientation is not horizontal when it is vertical`() {
        assertFalse { Orientation.VERTICAL.isHorizontal() }
    }

    @Test
    fun `Opposite orientation is vertical when orientation is horizontal`() {
        assertEquals(Orientation.VERTICAL, Orientation.HORIZONTAL.opposite())
    }

    @Test
    fun `Opposite orientation is horizontal when orientation is vertical`() {
        assertEquals(Orientation.HORIZONTAL, Orientation.VERTICAL.opposite())
    }
}
