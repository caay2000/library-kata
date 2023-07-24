package com.github.caay2000.projectskeleton.context.communication.application

import arrow.core.Either
import com.github.caay2000.projectskeleton.context.communication.domain.CommunicationRequest

interface CommunicationProcessor {

    fun invoke(communicationRequest: CommunicationRequest): Either<Throwable, Unit>
}
