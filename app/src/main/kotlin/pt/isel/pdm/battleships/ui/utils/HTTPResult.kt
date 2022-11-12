package pt.isel.pdm.battleships.ui.utils

/**
 * A result of an HTTP request.
 *
 * @param T the type of the result
 */
sealed class HTTPResult<T> {

    /**
     * A successful result.
     *
     * @param data the data of the result
     */
    class Success<T>(val data: T) : HTTPResult<T>()

    /**
     * A failed result.
     *
     * @param error the error associated with the result
     */
    class Failure<T>(val error: String) : HTTPResult<T>()
}
