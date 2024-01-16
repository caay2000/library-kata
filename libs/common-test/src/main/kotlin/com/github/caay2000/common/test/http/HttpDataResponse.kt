package com.github.caay2000.common.test.http

import com.github.caay2000.common.jsonapi.JsonApiErrorDocument
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText

data class HttpDataResponse<T>(
    val value: T?,
    val httpResponse: HttpResponse,
    val error: JsonApiErrorDocument?,
) {
    suspend fun body() = this.httpResponse.bodyAsText()
}
