@file:Suppress("OPT_IN_IS_NOT_ENABLED")

package pt.isel.pdm.battleships.service.connection

import pt.isel.pdm.battleships.service.media.Problem
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

/**
 * API Response result.
 *
 * @param T the type of the response
 */
sealed class APIResult<out T> {

    /**
     * The response was successful.
     *
     * @property data the response data
     */
    class Success<out T>(val data: T) : APIResult<T>()

    /**
     * The response was unsuccessful.
     *
     * @property error the Problem object
     */
    class Failure(val error: Problem) : APIResult<Nothing>()
}

/**
 * Checks if the api result is a success, and casts it accordingly.
 *
 * @receiver the api result to check
 * @return true if the result is a success, false otherwise
 **/
@OptIn(ExperimentalContracts::class)
fun <T> APIResult<T>.isSuccess(): Boolean {
    contract {
        returns(true) implies (this@isSuccess is APIResult.Success)
        returns(false) implies (this@isSuccess is APIResult.Failure)
    }
    return this is APIResult.Success
}

/**
 * Checks if the api result is a failure, and casts it accordingly.
 *
 * @receiver the api result to check
 * @return true if the result is a failure, false otherwise
 **/
@OptIn(ExperimentalContracts::class)
fun <T> APIResult<T>.isFailure(): Boolean {
    contract {
        returns(true) implies (this@isFailure is APIResult.Failure)
        returns(false) implies (this@isFailure is APIResult.Success)
    }
    return this is APIResult.Failure
}
