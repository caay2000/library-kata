package com.github.caay2000.projectskeleton.context.communication.communication.application

import arrow.core.Either
import com.github.caay2000.common.database.Repository
import com.github.caay2000.common.database.RepositoryError
import com.github.caay2000.projectskeleton.context.communication.communication.domain.CommunicationId
import com.github.caay2000.projectskeleton.context.communication.communication.domain.CommunicationRequest

interface CommunicationRequestRepository : Repository {

    fun save(communicationRequest: CommunicationRequest): Either<RepositoryError, Unit>
    fun findBy(criteria: FindCommunicationRequestCriteria): Either<RepositoryError, CommunicationRequest>
}

sealed class FindCommunicationRequestCriteria {
    class ByCommunicationId(val communicationId: CommunicationId) : FindCommunicationRequestCriteria()
}
