package com.github.caay2000.projectskeleton.context.communication.communication.secondaryadapter.communication

import arrow.core.Either
import com.github.caay2000.projectskeleton.context.communication.communication.application.send.CommunicationRequestEmailSender
import com.github.caay2000.projectskeleton.context.communication.communication.domain.Body
import com.github.caay2000.projectskeleton.context.communication.communication.domain.CommunicationId
import com.github.caay2000.projectskeleton.context.communication.communication.domain.Subject
import com.github.caay2000.projectskeleton.context.communication.contactdetails.domain.Email
import mu.KLogger
import mu.KotlinLogging

class LoggingCommunicationRequestEmailSender : CommunicationRequestEmailSender {

    private val logger: KLogger = KotlinLogging.logger {}

    override fun invoke(communicationId: CommunicationId, email: Email, subject: Subject, body: Body) {
        Either.catch { logger.info { "Email Sent: $communicationId, $email, $subject, $body" } }
    }
}
