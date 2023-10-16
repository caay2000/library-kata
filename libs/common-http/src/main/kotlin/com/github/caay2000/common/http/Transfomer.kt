package com.github.caay2000.common.http

import com.github.caay2000.common.jsonapi.JsonApiDocument

interface Transfomer<I, O : JsonApiDocument> {

    fun invoke(value: I, includes: List<String> = emptyList()): O

    fun <T> List<String>.shouldProcess(include: RequestInclude, block: () -> List<T>): List<T> =
        if (contains(include.toString().uppercase())) {
            block()
        } else {
            emptyList()
        }
}
