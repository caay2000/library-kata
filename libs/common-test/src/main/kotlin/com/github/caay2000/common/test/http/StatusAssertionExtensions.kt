package com.github.caay2000.common.test.http

import io.ktor.http.HttpStatusCode
import org.assertj.core.api.Assertions.assertThat

fun <T> HttpDataResponse<T>.assertStatus(status: HttpStatusCode): HttpDataResponse<T> =
    assertThat(httpResponse.status).isEqualTo(status)
        .let { this }

fun <T> HttpDataResponse<T>.assertResponse(response: T): HttpDataResponse<T> =
    assertThat(value).isEqualTo(response)
        .let { this }

fun <T> HttpDataResponse<T>.assertErrorMessage(message: String): HttpDataResponse<T> =
    assertThat(error?.message).isEqualTo(message)
        .let { this }
