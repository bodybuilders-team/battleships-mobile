package pt.isel.pdm.battleships.ui.screens.shared

import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import pt.isel.pdm.battleships.service.connection.APIResult
import pt.isel.pdm.battleships.service.connection.UnexpectedResponseException
import pt.isel.pdm.battleships.service.connection.isFailure
import pt.isel.pdm.battleships.service.media.Problem
import java.io.IOException

const val RETRY_DELAY = 1000L

/**
 * Initializes a [ViewModel].
 *
 * @receiver the activity that will hold the view model
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
 * Tries to execute a request to the server.
 * In case of [HTTPResult.Failure], the request will be retried after a delay [RETRY_DELAY].
 * If the server sends an error response ([APIResult.Failure]),
 * an [IllegalStateException] will be thrown with the error title.
 *
 * @param T the type of the data returned by the request
 * @param request the request to be executed
 * @param events the events container to send events to
 *
 * @return the result of the request
 * @throws IllegalStateException if the server sends an error response
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
        if (apiResult.isFailure())
            throw IllegalStateException(apiResult.error.title)

        return apiResult.data
    }
}

/**
 * Tries to execute a request to the server.
 * In case of [HTTPResult.Failure] or the server sending an error response ([APIResult.Failure]),
 * the request will be retried after a delay [RETRY_DELAY], depending on
 * [retryOnHttpResultFailure] and [retryOnApiResultFailure], respectively.
 *
 * @param T the type of the data returned by the request
 * @param request the request to be executed
 * @param events the events container to send events to
 * @param retryOnHttpResultFailure whether to retry the request in case of [HTTPResult.Failure].
 * May also be used to execute an action.
 * A message describing the error is sent as a parameter
 * @param retryOnApiResultFailure whether to retry the request in case of [APIResult.Failure].
 * May also be used to execute an action.
 * The [Problem] on the error response is sent as a parameter
 *
 * @return the result of the request, or null if the request failed (and was not retried)
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
            if (!retryOnHttpResultFailure(httpResult.error)) return null
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
 * Tries to execute a request to the server, in a coroutine of the [viewModelScope].
 * In case of [HTTPResult.Failure], the request will be retried after a delay [RETRY_DELAY].
 * If the server sends an error response ([APIResult.Failure]),
 * an [IllegalStateException] will be thrown with the error title.
 *
 * @receiver the view model executing the request
 * @param T the type of the data returned by the request
 * @param request the request to be executed
 * @param events the events container to send events to
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
 * Tries to execute a request to the server, in a coroutine of the [viewModelScope].
 * In case of [HTTPResult.Failure] or the server sending an error response ([APIResult.Failure]),
 * the request will be retried after a delay [RETRY_DELAY], depending on
 * [retryOnHttpResultFailure] and [retryOnApiResultFailure], respectively.
 *
 * @receiver the view model executing the request
 * @param T the type of the data returned by the request
 * @param request the request to be executed
 * @param events the events container to send events to
 * @param onSuccess the action to be executed when the result is a success
 * @param retryOnHttpResultFailure whether to retry the request in case of [HTTPResult.Failure].
 * May also be used to execute an action.
 * A message describing the error is sent as a parameter
 * @param retryOnApiResultFailure whether to retry the request in case of [APIResult.Failure].
 * May also be used to execute an action.
 * The [Problem] on the error response is sent as a parameter
 */
fun <T> ViewModel.launchAndExecuteRequest(
    request: suspend () -> APIResult<T>,
    events: MutableSharedFlow<Event>,
    onSuccess: suspend (T) -> Unit,
    retryOnHttpResultFailure: (String) -> Boolean = { true },
    retryOnApiResultFailure: (Problem) -> Boolean = { false }
) {
    viewModelScope.launch {
        executeRequest(request, events, retryOnHttpResultFailure, retryOnApiResultFailure)
            ?.also { onSuccess(it) }
    }
}

/**
 * Tries to execute the given request, returning a [HTTPResult.Success] if it executes successfully
 * or [HTTPResult.Failure] with an message describing the error if it throws an exception.
 *
 * @param T the type of result
 * @param request the request to execute
 *
 * @return the result of the request
 */
private suspend fun <T> tryExecuteHttpRequest(request: suspend () -> T): HTTPResult<T> =
    try {
        HTTPResult.Success(request())
    } catch (e: IOException) {
        HTTPResult.Failure(error = "Could not connect to the server.")
    } catch (e: UnexpectedResponseException) {
        HTTPResult.Failure(error = "Server sent an unexpected response.")
    }

/**
 * Handles an [HTTPResult].
 *
 * @receiver the [HTTPResult] to be handled
 * @param T the type of result
 * @param events the events container to send events to
 *
 * @return this [HTTPResult]
 */
suspend inline fun <T> HTTPResult<T>.handle(
    events: MutableSharedFlow<Event>
): HTTPResult<T> =
    apply {
        if (isFailure())
            events.emit(Event.Error(error))
    }

/**
 * Handles an [APIResult].
 *
 * @receiver the [APIResult] to be handled
 * @param T the type of result
 * @param events the events container to send events to
 *
 * @return this [APIResult]
 */
suspend inline fun <T> APIResult<T>.handle(
    events: MutableSharedFlow<Event>
): APIResult<T> =
    apply {
        if (isFailure())
            events.emit(Event.Error(error.title))
    }
