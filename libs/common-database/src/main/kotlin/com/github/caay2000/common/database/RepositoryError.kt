package com.github.caay2000.common.database

import arrow.core.Either

sealed class RepositoryError : RuntimeException() {
    class NotFoundError : RepositoryError()
}

fun <T> Either<Throwable, T>.mapRepositoryErrors() =
    this.mapLeft { error ->
        when (error) {
            is NullPointerException -> RepositoryError.NotFoundError()
            is NoSuchElementException -> RepositoryError.NotFoundError()
            else -> throw error
        }
    }
