package de.macnix.kutil.coroutines.timeout

import de.macnix.util.function.Either
import de.macnix.util.function.left
import de.macnix.util.function.right
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
