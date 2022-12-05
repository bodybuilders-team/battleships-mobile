@file:Suppress("OPT_IN_IS_NOT_ENABLED")

package pt.isel.pdm.battleships.ui.screens.shared

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

/**
 * A result of an HTTP request.
 *
 * @param T the type of the result
 */
sealed class HTTPResult<out T> {

    /**
     * A successful result.
     *
     * @param data the data of the result
     */
    class Success<out T>(val data: T) : HTTPResult<T>()

    /**
     * A failed result.
     *
     * @param error the error associated with the result
     */
    class Failure(val error: String) : HTTPResult<Nothing>()
}

/**
 * Checks if the http result is a success, and casts it accordingly.
 *
 * @return true if the result is a success, false otherwise
 */
@Suppress("unused")
@OptIn(ExperimentalContracts::class)
fun <T> HTTPResult<T>.isSuccess(): Boolean {
    contract {
        returns(true) implies (this@isSuccess is HTTPResult.Success)
        returns(false) implies (this@isSuccess is HTTPResult.Failure)
    }

    return this is HTTPResult.Success
}

/**
 * Checks if the http result is a failure, and casts it accordingly.
 *
 * @return true if the result is a failure, false otherwise
 */
@OptIn(ExperimentalContracts::class)
fun <T> HTTPResult<T>.isFailure(): Boolean {
    contract {
        returns(true) implies (this@isFailure is HTTPResult.Failure)
        returns(false) implies (this@isFailure is HTTPResult.Success)
    }

    return this is HTTPResult.Failure
}
