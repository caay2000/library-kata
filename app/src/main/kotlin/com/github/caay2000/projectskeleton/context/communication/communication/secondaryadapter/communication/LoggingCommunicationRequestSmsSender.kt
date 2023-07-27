package com.github.caay2000.projectskeleton.context.communication.communication.secondaryadapter.communication

import arrow.core.Either
import com.github.caay2000.projectskeleton.context.communication.communication.application.send.CommunicationRequestSmsSender
import com.github.caay2000.projectskeleton.context.communication.communication.domain.CommunicationId
import com.github.caay2000.projectskeleton.context.communication.communication.domain.Message
import com.github.caay2000.projectskeleton.context.communication.contactdetails.domain.PhoneNumber
import com.github.caay2000.projectskeleton.context.communication.contactdetails.domain.PhonePrefix
import mu.KLogger
import mu.KotlinLogging

class LoggingCommunicationRequestSmsSender : CommunicationRequestSmsSender {

    private val logger: KLogger = KotlinLogging.logger {}

    override fun invoke(communicationId: CommunicationId, phonePrefix: PhonePrefix, phoneNumber: PhoneNumber, message: Message) {
        Either.catch { logger.info { "SMS Sent: $communicationId, $phonePrefix, $phoneNumber, $message" } }
    }
}
