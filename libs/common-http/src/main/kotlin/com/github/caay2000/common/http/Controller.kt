package com.github.caay2000.common.http

import com.github.caay2000.common.jsonapi.ServerResponse
import com.github.caay2000.common.jsonapi.context.InvalidJsonApiException
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
            logger.error { e.message }
            handleExceptions(call, e)
        }
    }

    suspend fun handle(call: ApplicationCall)

    suspend fun handleExceptions(call: ApplicationCall, e: Exception) {
        val error = when (e) {
            is InvalidJsonApiException -> ServerResponse(HttpStatusCode.BadRequest, "InvalidJsonApiException", e.message)
            else -> ServerResponse(HttpStatusCode.InternalServerError)
        }
        call.respond(error.status, error.jsonApiErrorDocument)
    }

    suspend fun ApplicationCall.serverError(block: () -> ServerResponse) {
        val response = block()
        this.respond(response.status, response.jsonApiErrorDocument)
    }
}
