package com.github.caay2000.common.arrow

import arrow.core.Either
import arrow.core.getOrElse

fun <A : Throwable, B> Either<A, B>.getOrThrow(): B = this.getOrElse { throw it }
