package de.macnix.kutil.coroutines.timeout

import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.DescribeSpec
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds


class TimeoutSpec : DescribeSpec({
    val functionReturningAnInt1After3000MillisDelay: suspend () -> Int = {
        delay(3000)
        1
    }

    describe("tryWithTimeout()") {
        it("returns Either.Left(TimeoutError) if the task does not complete within the timeout period") {
            val timeout = 2.seconds
            val result = tryWithTimeout(timeout) {
                functionReturningAnInt1After3000MillisDelay.invoke()
            }
            println("Result: $result")
            result shouldBeLeft TimeoutError("Timed out waiting for ${timeout.inWholeMilliseconds} ms")
        }

        it("returns Either.Right(Int<1>) if the task completes within the timeout period") {
            val timeout = 4.seconds
            val result = tryWithTimeout(timeout) {
                functionReturningAnInt1After3000MillisDelay.invoke()
            }

            result shouldBeRight 1
        }

    }
})
