package pt.isel.pdm.battleships.services.utils

/**
 * HTTP Response result.
 */
sealed class Result<T> {

    /**
     * The response was successful.
     *
     * @property dto the response DTO
     */
    class Success<T>(val dto: T) : Result<T>()

    /**
     * The response was unsuccessful.
     *
     * @property error the error DTO
     */
    class Failure<T>(val error: ErrorDTO) : Result<T>()
}
