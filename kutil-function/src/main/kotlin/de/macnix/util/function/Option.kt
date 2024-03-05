package de.macnix.util.function

sealed class Option<out A> {
    fun isSome(): Boolean = this is Some
    fun isNone(): Boolean = this is None

    data class Some<A>(val value: A) : Option<A>() {
        override fun toString(): String = "Option.Some($value)"
    }

    object None : Option<Nothing>() {
        override fun toString(): String = "Option.None"
    }

    fun <A> identity(a: A): A = a

    companion object {
        fun <A> fromNullable(value: A?): Option<A> {
            return when (value) {
                null -> None
                else -> Some(value)
            }
        }
    }

}

inline fun <A> Option<A>.getOrElse(f: () -> A): A {
    return when (this) {
        is Option.None -> f()
        is Option.Some -> value
    }
}

inline fun <A> Option<A>.onSome(f: (a: A) -> Any?): Option<A> {
    when (this) {
        is Option.None -> {}
        is Option.Some -> f(this.value)
    }
    return this
}

inline fun <A> Option<A>.onNone(f: () -> Any?): Option<A> {
    when (this) {
        is Option.None -> f()
        is Option.Some -> {}
    }
    return this
}

fun <A> Option<Option<A>>.flatten(): Option<A> =
    flatMap(::identity)

inline fun <A, B> Option<A>.flatMap(f: (value: A) -> Option<B>): Option<B> {
    return when (this) {
        is Option.None -> this
        is Option.Some -> f(this.value)
    }
}

inline fun <A, B> Option<A>.map(f: (value: A) -> B): Option<B> {
    return when (this) {
        is Option.None -> this
        is Option.Some -> Option.Some(f(this.value))
    }
}

inline fun <A, B> Option<A>.fold(ifNone: () -> B, ifSome: (value: A) -> B): B {
    return when (this) {
        is Option.None -> ifNone()
        is Option.Some -> ifSome(this.value)
    }
}

fun <A> A.some(): Option<A> = Option.Some(this)
fun none() = Option.None
