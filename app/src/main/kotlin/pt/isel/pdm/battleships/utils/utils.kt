package pt.isel.pdm.battleships.utils // ktlint-disable filename

/**
 * Returns a new list with element at index [at] replaced by [new].
 *
 * @param at index to replace at
 * @param new element to replace previous
 */
fun <T> List<T>.replace(at: Int, new: T) =
    mapIndexed { idx, elem -> if (idx == at) new else elem }
