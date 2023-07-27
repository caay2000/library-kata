package com.github.caay2000.projectskeleton.context.communication.contactdetails.application.create

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.right
import com.github.caay2000.projectskeleton.context.communication.contactdetails.application.ContactDetailsRepository
import com.github.caay2000.projectskeleton.context.communication.contactdetails.domain.AccountNumber
import com.github.caay2000.projectskeleton.context.communication.contactdetails.domain.ContactDetails
import com.github.caay2000.projectskeleton.context.communication.contactdetails.domain.Email
import com.github.caay2000.projectskeleton.context.communication.contactdetails.domain.PhoneNumber
import com.github.caay2000.projectskeleton.context.communication.contactdetails.domain.PhonePrefix

class ContactDetailsCreator(private val contactDetailsRepository: ContactDetailsRepository) {

    fun invoke(
        accountNumber: AccountNumber,
        email: Email,
        phoneNumber: PhoneNumber,
        phonePrefix: PhonePrefix,
    ): Either<ContactDetailsCreatorError, Unit> =
        createContactDetails(accountNumber, email, phoneNumber, phonePrefix)
            .flatMap { account -> account.save() }

    private fun createContactDetails(
        accountNumber: AccountNumber,
        email: Email,
        phoneNumber: PhoneNumber,
        phonePrefix: PhonePrefix,
    ): Either<ContactDetailsCreatorError, ContactDetails> =
        ContactDetails.create(accountNumber, email, phoneNumber, phonePrefix).right()

    private fun ContactDetails.save(): Either<ContactDetailsCreatorError, Unit> =
        contactDetailsRepository.save(this)
            .mapLeft { ContactDetailsCreatorError.Unknown(it) }
}

sealed class ContactDetailsCreatorError : RuntimeException {
    constructor(throwable: Throwable) : super(throwable)

    class Unknown(error: Throwable) : ContactDetailsCreatorError(error)
}
