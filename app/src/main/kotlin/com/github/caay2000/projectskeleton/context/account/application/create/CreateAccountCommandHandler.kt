package com.github.caay2000.projectskeleton.context.account.application.create

import com.github.caay2000.common.arrow.getOrThrow
import com.github.caay2000.common.event.DomainEventPublisher
import com.github.caay2000.projectskeleton.common.cqrs.Command
import com.github.caay2000.projectskeleton.common.cqrs.CommandHandler
import com.github.caay2000.projectskeleton.context.account.application.AccountRepository
import com.github.caay2000.projectskeleton.context.account.domain.AccountId
import com.github.caay2000.projectskeleton.context.account.domain.Birthdate
import com.github.caay2000.projectskeleton.context.account.domain.CreateAccountRequest
import com.github.caay2000.projectskeleton.context.account.domain.Email
import com.github.caay2000.projectskeleton.context.account.domain.IdentityNumber
import com.github.caay2000.projectskeleton.context.account.domain.Name
import com.github.caay2000.projectskeleton.context.account.domain.PhoneNumber
import com.github.caay2000.projectskeleton.context.account.domain.PhonePrefix
import com.github.caay2000.projectskeleton.context.account.domain.RegisterDate
import com.github.caay2000.projectskeleton.context.account.domain.Surname
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

class CreateAccountCommandHandler(
    accountRepository: AccountRepository,
    eventPublisher: DomainEventPublisher,
) : CommandHandler<CreateAccountCommand> {

    private val creator = AccountCreator(accountRepository, eventPublisher)

    override fun invoke(command: CreateAccountCommand): Unit =
        creator.invoke(
            CreateAccountRequest(
                accountId = AccountId(command.accountId),
                identityNumber = IdentityNumber(command.identityNumber),
                name = Name(command.name),
                surname = Surname(command.surname),
                birthdate = Birthdate(command.birthdate),
                email = Email(command.email),
                phonePrefix = PhonePrefix(command.phonePrefix),
                phoneNumber = PhoneNumber(command.phoneNumber),
                registerDate = RegisterDate(command.registerDate),
            ),
        ).getOrThrow()
}

data class CreateAccountCommand(
    val accountId: UUID,
    val identityNumber: String,
    val email: String,
    val phoneNumber: String,
    val phonePrefix: String,
    val name: String,
    val surname: String,
    val birthdate: LocalDate,
    val registerDate: LocalDateTime,
) : Command
