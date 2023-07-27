package com.github.caay2000.projectskeleton.context.communication.contactdetails.application.create

import com.github.caay2000.archkata.common.cqrs.Command
import com.github.caay2000.archkata.common.cqrs.CommandHandler
import com.github.caay2000.common.arrow.getOrThrow
import com.github.caay2000.projectskeleton.context.communication.contactdetails.application.ContactDetailsRepository
import com.github.caay2000.projectskeleton.context.communication.contactdetails.domain.AccountNumber
import com.github.caay2000.projectskeleton.context.communication.contactdetails.domain.Email
import com.github.caay2000.projectskeleton.context.communication.contactdetails.domain.PhoneNumber
import com.github.caay2000.projectskeleton.context.communication.contactdetails.domain.PhonePrefix

class CreateContactDetailsCommandHandler(contactDetailsRepository: ContactDetailsRepository) : CommandHandler<CreateContactDetailsCommand> {

    private val creator = ContactDetailsCreator(contactDetailsRepository)

    override fun invoke(command: CreateContactDetailsCommand): Unit =
        creator.invoke(
            AccountNumber(command.accountNumber),
            Email(command.email),
            PhoneNumber(command.phoneNumber),
            PhonePrefix(command.phonePrefix),
        ).getOrThrow()
}

data class CreateContactDetailsCommand(
    val accountNumber: String,
    val email: String,
    val phoneNumber: String,
    val phonePrefix: String,
) : Command
