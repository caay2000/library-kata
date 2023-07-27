package com.github.caay2000.projectskeleton.context.communication.contactdetails.application

import arrow.core.Either
import com.github.caay2000.common.database.RepositoryError
import com.github.caay2000.projectskeleton.context.communication.contactdetails.domain.AccountNumber
import com.github.caay2000.projectskeleton.context.communication.contactdetails.domain.ContactDetails

interface ContactDetailsRepository {

    fun save(contactDetails: ContactDetails): Either<RepositoryError, Unit>
    fun findByAccountNumber(accountNumber: AccountNumber): Either<RepositoryError, ContactDetails>
}
