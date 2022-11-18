package pt.isel.pdm.battleships.ui.utils

import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import java.io.IOException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import pt.isel.pdm.battleships.services.exceptions.UnexpectedResponseException
import pt.isel.pdm.battleships.services.utils.APIResult
import pt.isel.pdm.battleships.services.utils.Problem
import pt.isel.pdm.battleships.services.utils.isFailure
import pt.isel.pdm.battleships.services.utils.isSuccess

const val RETRY_DELAY = 1000L

/**
 * Initializes a [ViewModel].
 *
 * @param T the type of the [ViewModel] to be initialized
 * @param block the block of code to be executed to initialize the [ViewModel]
 *
 * @return the initialized [ViewModel]
 */
@Suppress("UNCHECKED_CAST")
inline fun <reified T : ViewModel> ComponentActivity.viewModelInit(crossinline block: () -> T) =
    viewModels<T> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T = block() as T
        }
    }

/**
 * Tries to execute the given request and returns a [HTTPResult] with the result.
 *
 * If the request fails, the returned [HTTPResult] will be a [HTTPResult.Failure]
 * with the error message.
 *
 * @param executeRequest the request to execute
 * @return the result of the request
 */
suspend fun <T> tryExecuteHttpRequest(
    executeRequest: suspend () -> T
): HTTPResult<T> =
    try {
        HTTPResult.Success(executeRequest())
    } catch (e: IOException) {
        HTTPResult.Failure("Could not connect to the server.")
    } catch (e: UnexpectedResponseException) {
        HTTPResult.Failure("Server sent an unexpected response.")
    }

/**
 * Handles a [HTTPResult].
 *
 * @receiver the [HTTPResult] to be handled
 *
 * @param events the events that occurred in the view model
 *
 * @return the data of the result if it is a success, null otherwise
 */
suspend inline fun <T> HTTPResult<T>.handle(
    events: MutableSharedFlow<Event>
): HTTPResult<T> =
    if (this.isSuccess())
        HTTPResult.Success(this.data)
    else {
        events.emit(Event.Error(this.error))
        HTTPResult.Failure(this.error)
    }

/**
 * Handles a [APIResult].
 *
 * @receiver the [APIResult] to be handled
 *
 * @param events the events that occurred in the view model
 *
 * @return the data of the result if it is a success, null otherwise
 */
suspend inline fun <T> APIResult<T>.handle(
    events: MutableSharedFlow<Event>
): APIResult<T> =
    if (this.isSuccess())
        APIResult.Success(this.data)
    else {
        events.emit(Event.Error(this.error.title))
        APIResult.Failure(this.error)
    }

/**
 * Tries to execute an HTTP request.
 * If the request fails, it will be executed again.
 *
 * @param request the request to be executed
 * @param events the events that occurred in the view model
 *
 * @return the result of the request
 */
suspend fun <T> executeRequestThrowing(
    request: suspend () -> APIResult<T>,
    events: MutableSharedFlow<Event>
): T {
    while (true) {
        val httpResult = tryExecuteHttpRequest { request() }
            .handle(events = events)

        if (httpResult.isFailure()) {
            delay(RETRY_DELAY)
            continue
        }

        val apiResult = httpResult.data.handle(events = events)

        if (apiResult.isFailure()) throw IllegalStateException(apiResult.error.title)

        return apiResult.data
    }
}

/**
 * Tries to execute an HTTP request.
 * If the request fails, it will be executed again.
 *
 * @param request the request to be executed
 * @param events the events that occurred in the view model
 *
 * @return the result of the request
 */
suspend fun <T> executeRequest(
    request: suspend () -> APIResult<T>,
    events: MutableSharedFlow<Event>,
    retryOnHttpResultFailure: (String) -> Boolean = { true },
    retryOnApiResultFailure: (Problem) -> Boolean = { false }
): T? {
    while (true) {
        val httpResult = tryExecuteHttpRequest { request() }
            .handle(events = events)

        if (httpResult.isFailure()) {
            if (retryOnHttpResultFailure(httpResult.error)) return null
            delay(RETRY_DELAY)
            continue
        }

        val apiResult = httpResult.data.handle(events = events)

        if (apiResult.isFailure()) {
            if (!retryOnApiResultFailure(apiResult.error)) return null
            delay(RETRY_DELAY)
            continue
        }

        return apiResult.data
    }
}

/**
 * Tries to execute an HTTP request, in a coroutine.
 * If the request fails, it will be executed again.
 *
 * @param request the request to be executed
 * @param events the events that occurred in the view model
 * @param onSuccess the action to be executed when the result is a success
 */
fun <T> ViewModel.launchAndExecuteRequestThrowing(
    request: suspend () -> APIResult<T>,
    events: MutableSharedFlow<Event>,
    onSuccess: suspend (T) -> Unit
) {
    viewModelScope.launch {
        onSuccess(executeRequestThrowing(request, events))
    }
}

/**
 * Tries to execute an HTTP request, in a coroutine.
 * If the request fails, it will be executed again.
 *
 * @param request the request to be executed
 * @param events the events that occurred in the view model
 * @param onFinish the action to be executed when the result is a success or a failure
 */
fun <T> ViewModel.launchAndExecuteRequest(
    request: suspend () -> APIResult<T>,
    events: MutableSharedFlow<Event>,
    onFinish: suspend (T?) -> Unit,
    retryOnHttpResultFailure: (String) -> Boolean = { true },
    retryOnApiResultFailure: (Problem) -> Boolean = { true }
) {
    viewModelScope.launch {
        onFinish(
            executeRequest(
                request,
                events,
                retryOnHttpResultFailure,
                retryOnApiResultFailure
            )
        )
    }
}
