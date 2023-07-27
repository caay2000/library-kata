package com.github.caay2000.projectskeleton.context.communication.communication.secondaryadapter.database

import arrow.core.Either
import com.github.caay2000.common.database.RepositoryError
import com.github.caay2000.memorydb.InMemoryDatasource
import com.github.caay2000.projectskeleton.context.communication.communication.application.CommunicationRequestRepository
import com.github.caay2000.projectskeleton.context.communication.communication.application.FindCommunicationRequestCriteria
import com.github.caay2000.projectskeleton.context.communication.communication.domain.CommunicationRequest

class InMemoryCommunicationRequestRepository(private val datasource: InMemoryDatasource) : CommunicationRequestRepository {

    companion object {
        private const val TABLE_NAME = "communication.communication_request"
    }

    override fun save(communicationRequest: CommunicationRequest): Either<RepositoryError, Unit> =
        Either.catch { datasource.save(TABLE_NAME, communicationRequest.id.toString(), communicationRequest) }
            .mapLeft { RepositoryError.Unknown(it) }
            .map { }

    override fun findBy(criteria: FindCommunicationRequestCriteria): Either<RepositoryError, CommunicationRequest> =
        Either.catch {
            when (criteria) {
                is FindCommunicationRequestCriteria.ByCommunicationId -> datasource.getById<CommunicationRequest>(TABLE_NAME, criteria.communicationId.toString())!!
            }
        }.mapLeft { error ->
            when (error) {
                is NullPointerException -> RepositoryError.NotFoundError()
                is NoSuchElementException -> RepositoryError.NotFoundError()
                else -> RepositoryError.Unknown(error)
            }
        }
}
