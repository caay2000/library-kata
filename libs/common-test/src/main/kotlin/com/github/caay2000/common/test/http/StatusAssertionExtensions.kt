package com.github.caay2000.common.test.http

import io.ktor.http.HttpStatusCode
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.assertj.core.api.Assertions.assertThat
import org.skyscreamer.jsonassert.JSONAssert

fun <T> HttpDataResponse<T>.assertStatus(status: HttpStatusCode): HttpDataResponse<T> =
    assertThat(httpResponse.status).withFailMessage { error?.message }.isEqualTo(status)
        .let { this }

fun <T> HttpDataResponse<T>.assertResponse(response: T): HttpDataResponse<T> =
    assertThat(value).isEqualTo(response)
        .let { this }

inline fun <reified T> HttpDataResponse<T>.assertJsonResponse(response: T): HttpDataResponse<T> =
    JSONAssert.assertEquals(Json.encodeToString(value), Json.encodeToString(response), true)
        .let { this }

fun <T> HttpDataResponse<T>.assertErrorMessage(message: String): HttpDataResponse<T> =
    assertThat(error?.message).isEqualTo(message)
        .let { this }
