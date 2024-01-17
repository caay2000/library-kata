package com.github.caay2000.common.http

interface Transformer<I, O> {
    fun invoke(
        value: I,
        include: List<String> = emptyList(),
    ): O

    fun <T> List<String>.shouldProcess(
        include: RequestInclude,
        block: () -> List<T>?,
    ): List<T>? =
        if (contains(include.toString().uppercase())) {
            block()
        } else {
            null
        }
}
