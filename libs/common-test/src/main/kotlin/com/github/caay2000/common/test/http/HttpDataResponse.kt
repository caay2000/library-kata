package com.github.caay2000.common.test.http

import com.github.caay2000.common.http.ErrorResponseDocument
import io.ktor.client.statement.HttpResponse

data class HttpDataResponse<T>(
    val value: T?,
    val httpResponse: HttpResponse,
    val error: ErrorResponseDocument?,
)
