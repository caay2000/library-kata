package com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http

import com.github.caay2000.common.http.ErrorResponseDocument
import com.github.caay2000.common.http.HttpErrorResponse
import io.bkbn.kompendium.core.plugin.NotarizedRoute
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import mu.KLogger

interface OpenApiController {

    val logger: KLogger

    val method: HttpMethod
    val path: String

    fun NotarizedRoute.Config.documentation() {
    }

    fun Route.populateOpenApi(): Route {
        install(NotarizedRoute()) {
            documentation()
        }
        return this
    }

    fun init(routing: Routing) {
        routing.get(path) { invoke(this.call) }.populateOpenApi()
    }

    suspend fun invoke(call: ApplicationCall) {
        try {
            handle(call)
        } catch (e: Exception) {
            val response = handleExceptions(call, e)
            call.respond(
                response.status,
                ErrorResponseDocument(response.message),
            )
        }
    }

    suspend fun handle(call: ApplicationCall)

    suspend fun handleExceptions(call: ApplicationCall, e: Exception) =
        HttpErrorResponse(HttpStatusCode.InternalServerError, e.message ?: "Unknown error")
}
