package com.github.caay2000.common.http

import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.call
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import io.ktor.util.pipeline.PipelineContext

fun Routing.get(path: String, queryParam: String, block: suspend PipelineContext<Unit, ApplicationCall>.(Unit) -> Unit) {
    get(path) {
        if (this.call.request.queryParameters[queryParam] != null) block(this, Unit)
    }
}
