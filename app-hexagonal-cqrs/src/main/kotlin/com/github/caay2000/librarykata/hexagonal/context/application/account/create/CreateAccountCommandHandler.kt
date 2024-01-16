package com.github.caay2000.librarykata.hexagonal.context.application.account.create

import com.github.caay2000.common.arrow.getOrThrow
import com.github.caay2000.common.cqrs.Command
import com.github.caay2000.common.cqrs.CommandHandler
import com.github.caay2000.librarykata.hexagonal.context.domain.account.AccountId
import com.github.caay2000.librarykata.hexagonal.context.domain.account.AccountRepository
import com.github.caay2000.librarykata.hexagonal.context.domain.account.Birthdate
import com.github.caay2000.librarykata.hexagonal.context.domain.account.CreateAccountRequest
import com.github.caay2000.librarykata.hexagonal.context.domain.account.Email
import com.github.caay2000.librarykata.hexagonal.context.domain.account.IdentityNumber
import com.github.caay2000.librarykata.hexagonal.context.domain.account.Name
import com.github.caay2000.librarykata.hexagonal.context.domain.account.PhoneNumber
import com.github.caay2000.librarykata.hexagonal.context.domain.account.PhonePrefix
import com.github.caay2000.librarykata.hexagonal.context.domain.account.RegisterDate
import com.github.caay2000.librarykata.hexagonal.context.domain.account.Surname
import mu.KLogger
import mu.KotlinLogging
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

class CreateAccountCommandHandler(
    accountRepository: AccountRepository,
) : CommandHandler<CreateAccountCommand> {
    override val logger: KLogger = KotlinLogging.logger {}
    private val creator = AccountCreator(accountRepository)

    override fun handle(command: CreateAccountCommand): Unit =
        creator.invoke(
            CreateAccountRequest(
                accountId = AccountId(command.accountId.toString()),
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
