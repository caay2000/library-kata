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

inline fun <reified T> HttpDataResponse<T>.assertJsonResponse(response: String, mapper: Json = Json): HttpDataResponse<T> =
    try {
        JSONAssert.assertEquals(mapper.encodeToString(value), response, true).let { this }
    } catch (e: Throwable) {
        logger.warn { "expected: ${mapper.encodeToString(value)}" }
        logger.warn { "actual  : ${mapper.encodeToString(response)}" }
        throw e
    }

inline fun <reified T> HttpDataResponse<T>.assertJsonResponse(response: T, mapper: Json = Json): HttpDataResponse<T> =
    try {
        JSONAssert.assertEquals(mapper.encodeToString(value), mapper.encodeToString(response), true).let { this }
    } catch (e: Throwable) {
        logger.warn { "expected: ${mapper.encodeToString(value)}" }
        logger.warn { "actual  : ${mapper.encodeToString(response)}" }
        throw e
    }

inline fun <reified T> HttpDataResponse<T>.printJsonResponse(mapper: Json = Json): HttpDataResponse<T> =
    try {
        logger.info { mapper.encodeToString(this.value!!) }
        this
    } catch (e: Throwable) {
        logger.warn { "Impossible to log JsonResponse due to ${e.message}" }
        this
    }

fun <T> HttpDataResponse<T>.assertErrorMessage(message: String): HttpDataResponse<T> =
    assertThat(error?.message).isEqualTo(message)
        .let { this }
