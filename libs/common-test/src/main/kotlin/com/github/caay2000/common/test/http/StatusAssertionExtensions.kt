package com.github.caay2000.common.test.http

import io.ktor.http.HttpStatusCode
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import mu.KLogger
import mu.KotlinLogging
import org.assertj.core.api.Assertions.assertThat
import org.skyscreamer.jsonassert.JSONAssert

val logger: KLogger = KotlinLogging.logger {}

fun <T> HttpDataResponse<T>.assertStatus(status: HttpStatusCode): HttpDataResponse<T> =
    assertThat(httpResponse.status).withFailMessage { error?.message }.isEqualTo(status)
        .let { this }

fun <T> HttpDataResponse<T>.assertResponse(response: T): HttpDataResponse<T> =
    assertThat(value).isEqualTo(response)
        .let { this }

inline fun <reified T> HttpDataResponse<T>.assertJsonResponse(response: T): HttpDataResponse<T> =
    try {
        JSONAssert.assertEquals(Json.encodeToString(value), Json.encodeToString(response), true).let { this }
    } catch (e: Throwable) {
        logger.warn { "expected: ${Json.encodeToString(value)}" }
        logger.warn { "actual  : ${Json.encodeToString(response)}" }
        throw e
    }

fun <T> HttpDataResponse<T>.assertErrorMessage(message: String): HttpDataResponse<T> =
    assertThat(error?.message).isEqualTo(message)
        .let { this }
