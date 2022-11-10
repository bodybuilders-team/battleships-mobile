package pt.isel.pdm.battleships.services.utils

/**
 * HTTP Response result.
 */
sealed class HTTPResult<T> {

    /**
     * The response was successful.
     *
     * @property data the response data
     */
    class Success<T>(val data: T) : HTTPResult<T>()

    /**
     * The response was unsuccessful.
     *
     * @property error the error DTO
     */
    class Failure<T>(val error: Problem) : HTTPResult<T>()
}
