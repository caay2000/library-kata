package com.github.caay2000.common.test.http

import com.github.caay2000.common.jsonapi.JsonApiErrorDocument
import com.github.caay2000.common.test.json.JsonApiSchemaValidator
import io.ktor.http.HttpStatusCode
import mu.KLogger
import mu.KotlinLogging
import org.assertj.core.api.Assertions.assertThat
import org.skyscreamer.jsonassert.JSONAssert

val logger: KLogger = KotlinLogging.logger {}

fun <T> HttpDataResponse<T>.assertStatus(status: HttpStatusCode): HttpDataResponse<T> =
    assertThat(httpResponse.status).isEqualTo(status)
        .let { this }

fun <T> HttpDataResponse<T>.assertResponse(response: T): HttpDataResponse<T> =
    assertThat(value).isEqualTo(response)
        .let { this }

suspend inline fun <reified T> HttpDataResponse<T>.assertJsonApiResponse(expected: String): HttpDataResponse<T> =
    with(body()) {
        try {
            JSONAssert.assertEquals(expected, this, true).let { this }
            this@assertJsonApiResponse.assureJsonApiSchema()
        } catch (e: Throwable) {
            logger.warn { "expected: $expected" }
            logger.warn { "actual  : $this" }
            throw e
        }
    }

suspend inline fun <reified T> HttpDataResponse<T>.printJsonResponse(): HttpDataResponse<T> =
    try {
        val body = body()
        logger.info { body }
        this
    } catch (e: Throwable) {
        logger.warn { "Impossible to log JsonResponse due to ${e.message}" }
        this
    }

@JvmName("assureJsonApiSchemaForResponse")
suspend inline fun <reified T> HttpDataResponse<T>.assureJsonApiSchema(): HttpDataResponse<T> =
    run {
        val list = JsonApiSchemaValidator().validate("jsonApiSchema_v1_1.json", body())
        if (list.isNotEmpty()) {
            logger.error { list }
            throw RuntimeException(list.toString())
        }
        this
    }

suspend inline fun <reified T> HttpDataResponse<T>.assertJsonApiErrorDocument(expected: JsonApiErrorDocument): HttpDataResponse<T> =
    assertThat(error).isEqualTo(expected)
        .let { this }
        .assureJsonApiSchema()
