package com.github.caay2000.projectskeleton.context.account.application.create

import com.github.caay2000.archkata.common.cqrs.Command
import com.github.caay2000.archkata.common.cqrs.CommandHandler
import com.github.caay2000.common.arrow.getOrThrow
import com.github.caay2000.common.event.api.DomainEventPublisher
import com.github.caay2000.projectskeleton.context.account.application.AccountRepository
import com.github.caay2000.projectskeleton.context.account.domain.AccountNumber
import com.github.caay2000.projectskeleton.context.account.domain.BirthDate
import com.github.caay2000.projectskeleton.context.account.domain.CreateAccountRequest
import com.github.caay2000.projectskeleton.context.account.domain.Email
import com.github.caay2000.projectskeleton.context.account.domain.Name
import com.github.caay2000.projectskeleton.context.account.domain.PhoneNumber
import com.github.caay2000.projectskeleton.context.account.domain.PhonePrefix
import com.github.caay2000.projectskeleton.context.account.domain.RegisterDate
import com.github.caay2000.projectskeleton.context.account.domain.Surname
import java.time.LocalDate
import java.time.LocalDateTime

class CreateAccountCommandHandler(
    accountRepository: AccountRepository,
    eventPublisher: DomainEventPublisher,
) : CommandHandler<CreateAccountCommand> {

    private val creator = AccountCreator(accountRepository, eventPublisher)

    override fun invoke(command: CreateAccountCommand): Unit =
        creator.invoke(
            CreateAccountRequest(
                accountNumber = AccountNumber(command.accountNumber),
                email = Email(command.email),
                phoneNumber = PhoneNumber(command.phoneNumber),
                phonePrefix = PhonePrefix(command.phonePrefix),
                name = Name(command.name),
                surname = Surname(command.surname),
                birthDate = BirthDate(command.birthDate),
                registerDate = RegisterDate(command.registerDate),
            ),
        ).getOrThrow()
}

data class CreateAccountCommand(
    val accountNumber: String,
    val email: String,
    val phoneNumber: String,
    val phonePrefix: String,
    val name: String,
    val surname: String,
    val birthDate: LocalDate,
    val registerDate: LocalDateTime,
) : Command
