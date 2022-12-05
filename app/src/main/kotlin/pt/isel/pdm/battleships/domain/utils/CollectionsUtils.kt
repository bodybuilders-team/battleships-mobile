package pt.isel.pdm.battleships.domain.utils

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

/**
 * Returns a new list with elements that satisfy [predicate] replaced by [new].
 *
 * @param predicate predicate to test elements
 * @param new element to replace previous
 *
 * @return new list with elements that satisfy [predicate] replaced by [new]
 */
fun <T> List<T>.replaceIf(predicate: (T) -> Boolean, new: (T) -> T) =
    map { elem -> if (predicate(elem)) new(elem) else elem }

/**
 * Returns the first value whose key matches the given [predicate],
 * or null if no such element was found.
 *
 * @param predicate predicate to test keys
 *
 * @return first value whose key abides to [predicate]
 */
fun <K, V> Map<K, V>.findValueByKey(predicate: (K) -> Boolean): V? {
    for (entry in this) {
        if (predicate(entry.key))
            return entry.value
    }

    return null
}
