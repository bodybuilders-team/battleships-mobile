package pt.isel.pdm.battleships.services.utils

/**
 * API Response result.
 */
sealed class APIResult<T> {

    /**
     * The response was successful.
     *
     * @property data the response data
     */
    class Success<T>(val data: T) : APIResult<T>()

    /**
     * The response was unsuccessful.
     *
     * @property error the Problem object
     */
    class Failure<T>(val error: Problem) : APIResult<T>()
}
