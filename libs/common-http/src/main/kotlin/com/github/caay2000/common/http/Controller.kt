package com.github.caay2000.common.http

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.respond
import mu.KLogger

interface Controller {

    val logger: KLogger

    suspend operator fun invoke(call: ApplicationCall) {
        try {
            handle(call)
        } catch (e: Exception) {
            try {
                val response = handleExceptions(call, e)
                call.respond(
                    response.status,
                    ErrorResponseDocument(response.message),
                )
            } catch (e: Exception) {
                logger.error { "error: $e" }
                call.respond(HttpStatusCode.InternalServerError)
            }
        }
    }

    suspend fun handle(call: ApplicationCall)

    suspend fun handleExceptions(call: ApplicationCall, e: Exception) =
        HttpErrorResponse(HttpStatusCode.InternalServerError, e.message ?: "Unknown error")
}

data class HttpErrorResponse(
    val status: HttpStatusCode,
    val message: String,
)
