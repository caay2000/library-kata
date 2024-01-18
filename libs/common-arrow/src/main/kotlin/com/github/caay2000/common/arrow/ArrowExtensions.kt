package com.github.caay2000.common.arrow

import arrow.core.Either
import arrow.core.getOrElse
import arrow.core.left
import arrow.core.right

fun <A : Throwable, B> Either<A, B>.getOrThrow(): B = this.getOrElse { throw it }

fun <T, E> Iterable<T>.firstOrElse(
    predicate: (T) -> Boolean,
    onError: () -> E,
): Either<E, T> = this.firstOrNull(predicate).let { it?.right() ?: onError().left() }
