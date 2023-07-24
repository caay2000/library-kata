package com.github.caay2000.projectskeleton.context.account.application.create

import com.github.caay2000.archkata.common.cqrs.Command
import com.github.caay2000.archkata.common.cqrs.CommandHandler
import com.github.caay2000.common.arrow.getOrThrow
import com.github.caay2000.common.event.api.DomainEventPublisher
import com.github.caay2000.projectskeleton.context.account.application.AccountRepository
import com.github.caay2000.projectskeleton.context.account.domain.AccountId
import com.github.caay2000.projectskeleton.context.account.domain.CreateAccountRequest
import com.github.caay2000.projectskeleton.context.account.domain.Email
import com.github.caay2000.projectskeleton.context.account.domain.Name
import java.util.UUID

class CreateAccountCommandHandler(
    accountRepository: AccountRepository,
    eventPublisher: DomainEventPublisher,
) : CommandHandler<CreateAccountCommand> {

    private val creator = AccountCreator(accountRepository, eventPublisher)

    override fun invoke(command: CreateAccountCommand): Unit =
        creator.invoke(
            CreateAccountRequest(
                id = AccountId(command.accountId),
                email = Email(command.email),
                name = Name(command.name),
            ),
        ).getOrThrow()
}

data class CreateAccountCommand(val accountId: UUID, val email: String, val name: String) : Command
