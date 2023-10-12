package com.github.caay2000.common.test.http

import com.github.caay2000.common.serialization.defaultObjectMapper
import io.ktor.http.HttpStatusCode
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
        val actual = defaultObjectMapper().writeValueAsString(value)
        val expected = defaultObjectMapper().writeValueAsString(response)
        JSONAssert.assertEquals(expected, actual, true).let { this }
    } catch (e: Throwable) {
        logger.warn { "expected: ${defaultObjectMapper().writeValueAsString(value)}" }
        logger.warn { "actual  : ${defaultObjectMapper().writeValueAsString(value)}" }
        throw e
    }

inline fun <reified T> HttpDataResponse<T>.printJsonResponse(): HttpDataResponse<T> =
    try {
        logger.info { defaultObjectMapper().writeValueAsString(this.value!!) }
        this
    } catch (e: Throwable) {
        logger.warn { "Impossible to log JsonResponse due to ${e.message}" }
        this
    }

fun <T> HttpDataResponse<T>.assertErrorMessage(message: String): HttpDataResponse<T> =
    assertThat(error?.message).isEqualTo(message)
        .let { this }
