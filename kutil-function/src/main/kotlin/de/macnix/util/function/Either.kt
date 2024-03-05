package de.macnix.util.function

sealed class Either<out A, out B> {
    fun isRight(): Boolean = this@Either is Right
    fun isLeft(): Boolean = this@Either is Left

    data class Left<A>(val left: A) : Either<A, Nothing>() {
        override fun toString(): String = "Either.Left($left)"
    }

    data class Right<B>(val right: B) : Either<Nothing, B>() {
        override fun toString(): String = "Either.Right($right)"
    }

    fun <A> identity(a: A): A = a

}


inline fun <A, B, C> Either<A, B>.fold(ifLeft: (left: A) -> C, ifRight: (right: B) -> C): C {
    return when (this) {
        is Either.Left -> ifLeft(left)
        is Either.Right -> ifRight(right)
    }
}


inline fun <A, B, C> Either<A, B>.flatMap(f: (right: B) -> Either<A, C>): Either<A, C> {
    return when (this) {
        is Either.Right -> f(this.right)
        is Either.Left -> this
    }
}

inline fun <A, B, C> Either<A, B>.map(f: (right: B) -> C): Either<A, C> {
    return when (this) {
        is Either.Left -> this
        is Either.Right -> Either.Right(f(right))
    }
}

fun <A, B> Either<A, Either<A, B>>.flatten(): Either<A, B> =
    flatMap(::identity)

fun <A> A.left(): Either<A, Nothing> = Either.Left(this)
fun <A> A.right(): Either<Nothing, A> = Either.Right(this)

fun <A, B> Either<A, B>.onLeft(f: (a: A) -> Any?): Either<A, B> {
    when (this) {
        is Either.Left -> f(this.left)
        is Either.Right -> {}
    }
    return this
}

fun <A, B> Either<A, B>.onRight(f: (b: B) -> Any?): Either<A, B> {
    when (this) {
        is Either.Right -> f(this.right)
        is Either.Left -> {}
    }
    return this
}

inline fun <A> catch(f: () -> A): Either<Throwable, A> {
    return try {
        Either.Right(f())
    } catch (t: Throwable) {
        Either.Left(t)
    }
}