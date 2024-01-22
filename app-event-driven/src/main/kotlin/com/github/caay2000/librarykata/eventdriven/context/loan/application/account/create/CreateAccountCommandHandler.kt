package com.github.caay2000.librarykata.eventdriven.context.loan.application.account.create

import com.github.caay2000.common.cqrs.Command
import com.github.caay2000.common.cqrs.CommandHandler
import com.github.caay2000.librarykata.eventdriven.context.loan.domain.AccountId
import com.github.caay2000.librarykata.eventdriven.context.loan.domain.AccountRepository
import mu.KLogger
import mu.KotlinLogging

class CreateAccountCommandHandler(accountRepository: AccountRepository) : CommandHandler<CreateAccountCommand> {
    override val logger: KLogger = KotlinLogging.logger {}
    private val creator = AccountCreator(accountRepository)

    override fun handle(command: CreateAccountCommand): Unit = creator.invoke(accountId = command.accountId)
}

data class CreateAccountCommand(val accountId: AccountId) : Command
