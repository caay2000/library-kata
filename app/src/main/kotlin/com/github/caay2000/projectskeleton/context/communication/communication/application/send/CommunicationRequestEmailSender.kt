package com.github.caay2000.projectskeleton.context.communication.communication.application.send

import com.github.caay2000.projectskeleton.context.communication.communication.domain.Body
import com.github.caay2000.projectskeleton.context.communication.communication.domain.CommunicationId
import com.github.caay2000.projectskeleton.context.communication.communication.domain.Subject
import com.github.caay2000.projectskeleton.context.communication.contactdetails.domain.Email

interface CommunicationRequestEmailSender {

    fun invoke(communicationId: CommunicationId, email: Email, subject: Subject, body: Body)
}
