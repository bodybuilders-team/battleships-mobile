package pt.isel.pdm.battleships.utils

/**
 * Returns a new list with element at index [at] replaced by [new].
 *
 * @param at index to replace at
 * @param new element to replace previous
 *
 * @return new list with element at index [at] replaced by [new]
 */
fun <T> List<T>.replace(at: Int, new: T) =
    mapIndexed { idx, elem -> if (idx == at) new else elem }
