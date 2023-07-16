package de.macnix.kutil.coroutines.timeout

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import kotlinx.coroutines.withTimeout
import kotlin.time.Duration

data class TimeoutError(val message: String)

suspend fun <T> tryWithTimeout(
    timeout: Duration,
    task: suspend () -> T
): Either<TimeoutError, T> {
    return try {
        withTimeout(timeout) { task.invoke().right() }
    } catch (e: Throwable) {
        TimeoutError(e.localizedMessage).left()
    }
}
