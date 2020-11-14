package com.corroutines.ioTests

sealed class Result<out T, out E>

data class Success<out T>(val value: T) : Result<T, Nothing>()
data class Failure<out E>(val reason: E) : Result<Nothing, E>()

fun <T, X : Throwable> Result<T, X>.orThrow() = when (this) {
    is Success<T> -> value
    is Failure<X> -> throw reason
}

inline fun <T, E> Result<T, E>.onFailure(block: (Failure<E>) -> Nothing): T = when (this) {
    is Success<T> -> value
    is Failure<E> -> block(this)
}

fun <T, E> Result<T, E>.getOrNull() = when(this){
    is Success<T> -> value
    is Failure<E> -> null
}