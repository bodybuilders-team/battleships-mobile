package pt.isel.pdm.battleships.domain.utils

import kotlin.test.Test
import kotlin.test.assertEquals

class ReplaceUtilsTests {

    @Test
    fun `replace element at index`() {
        val list = listOf(1, 2, 3, 4, 5)
        val expected = listOf(1, 2, 3, 6, 5)
        val actual = list.replace(3, 6)
        assertEquals(expected, actual)
    }

    @Test
    fun `replace element at index out of bounds`() {
        val list = listOf(1, 2, 3, 4, 5)
        val actual = list.replace(5, 6)
        assertEquals(list, actual)
    }

    @Test
    fun `replace element if predicate is true`() {
        val list = listOf(1, 2, 3, 4, 5)
        val expected = listOf(1, 2, 3, 6, 5)
        val actual = list.replaceIf({ it == 4 }, { 6 })
        assertEquals(expected, actual)
    }

    @Test
    fun `replace element if predicate is false`() {
        val list = listOf(1, 2, 3, 4, 5)
        val actual = list.replaceIf({ it == 6 }, { 6 })
        assertEquals(list, actual)
    }
}
