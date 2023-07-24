package com.github.caay2000.projectskeleton.context.communication.secondaryadapter.communication

import arrow.core.Either
import com.github.caay2000.projectskeleton.context.communication.application.CommunicationProcessor
import com.github.caay2000.projectskeleton.context.communication.domain.CommunicationRequest
import mu.KLogger
import mu.KotlinLogging

class LoggingCommunicationProcessor : CommunicationProcessor {

    private val logger: KLogger = KotlinLogging.logger {}

    override fun invoke(communicationRequest: CommunicationRequest): Either<Throwable, Unit> =
        Either.catch { logger.info { "CommunicationSent: $communicationRequest" } }
}
