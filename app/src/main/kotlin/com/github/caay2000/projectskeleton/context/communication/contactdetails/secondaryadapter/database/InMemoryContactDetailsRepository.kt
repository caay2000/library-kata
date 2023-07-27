package com.github.caay2000.projectskeleton.context.communication.contactdetails.secondaryadapter.database

import arrow.core.Either
import com.github.caay2000.common.database.RepositoryError
import com.github.caay2000.memorydb.InMemoryDatasource
import com.github.caay2000.projectskeleton.context.communication.contactdetails.application.ContactDetailsRepository
import com.github.caay2000.projectskeleton.context.communication.contactdetails.domain.AccountNumber
import com.github.caay2000.projectskeleton.context.communication.contactdetails.domain.ContactDetails

class InMemoryContactDetailsRepository(private val datasource: InMemoryDatasource) : ContactDetailsRepository {

    companion object {
        private const val TABLE_NAME = "communication.contact_details"
    }

    override fun save(contactDetails: ContactDetails): Either<RepositoryError, Unit> =
        Either.catch { datasource.save(TABLE_NAME, contactDetails.accountNumber.toString(), contactDetails) }
            .mapLeft { RepositoryError.Unknown(it) }
            .map { }

    override fun findByAccountNumber(accountNumber: AccountNumber): Either<RepositoryError, ContactDetails> =
        Either.catch { datasource.getById<ContactDetails>(TABLE_NAME, accountNumber.toString())!! }
            .mapLeft { error ->
                when (error) {
                    is NullPointerException -> RepositoryError.NotFoundError()
                    is NoSuchElementException -> RepositoryError.NotFoundError()
                    else -> RepositoryError.Unknown(error)
                }
            }
}
