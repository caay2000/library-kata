package com.github.caay2000.projectskeleton.context.communication.communication.application.send

import com.github.caay2000.projectskeleton.context.communication.communication.domain.CommunicationId
import com.github.caay2000.projectskeleton.context.communication.communication.domain.Message
import com.github.caay2000.projectskeleton.context.communication.contactdetails.domain.PhoneNumber
import com.github.caay2000.projectskeleton.context.communication.contactdetails.domain.PhonePrefix

interface CommunicationRequestSmsSender {

    fun invoke(communicationId: CommunicationId, phonePrefix: PhonePrefix, phoneNumber: PhoneNumber, message: Message)
}
