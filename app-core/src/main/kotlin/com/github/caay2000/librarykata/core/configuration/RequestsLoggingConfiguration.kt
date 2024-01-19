package com.github.caay2000.librarykata.core.configuration

import com.github.caay2000.common.http.Controller
import com.github.caay2000.common.idgenerator.UUIDGenerator
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationCallPipeline
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.plugins.callid.CallId
import io.ktor.server.plugins.callid.callIdMdc
import io.ktor.server.plugins.callloging.CallLogging
import io.ktor.server.plugins.doublereceive.DoubleReceive
import io.ktor.server.request.httpMethod
import io.ktor.server.request.receiveText
import io.ktor.server.request.uri
import io.ktor.util.AttributeKey
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging
import kotlin.time.TimeMark
import kotlin.time.TimeSource

fun Application.requestLoggingConfiguration() {
    val controllerLogger = KotlinLogging.logger(Controller::class.java.canonicalName)
    val callStartTimeAttributeKey = AttributeKey<TimeMark>("CallStartTime2")

    install(DoubleReceive)
    install(CallId) { generate { UUIDGenerator().generate() } }

    intercept(ApplicationCallPipeline.Setup) {
        call.attributes.put(callStartTimeAttributeKey, TimeSource.Monotonic.markNow())
    }

    intercept(ApplicationCallPipeline.Monitoring) {
        val httpMethod = call.request.httpMethod.value
        val httpUri = call.request.uri
        controllerLogger.info { "$httpMethod $httpUri" }
        val requestBody = runBlocking { call.receiveText() }
        if (requestBody.isNotBlank()) {
            controllerLogger.debug { "Request Body:\n$requestBody" }
        }
    }

    install(CallLogging) {
        callIdMdc("correlationId")
        logger = controllerLogger
        format { call ->
            val httpStatus = call.response.status()
            val httpMethod = call.request.httpMethod.value
            val httpUri = call.request.uri
            val startTime = call.attributes.getOrNull(callStartTimeAttributeKey)

            // TODO print response body as DEBUG like in request
            "$httpMethod $httpUri - ${httpStatus?.value} ${httpStatus?.description} in ${startTime?.elapsedNow()}"
        }
    }
}
