package com.github.caay2000.librarykata.eventdriven.context.account.application.update

import com.github.caay2000.common.arrow.getOrThrow
import com.github.caay2000.common.cqrs.Command
import com.github.caay2000.common.cqrs.CommandHandler
import com.github.caay2000.librarykata.eventdriven.context.account.domain.AccountId
import com.github.caay2000.librarykata.eventdriven.context.account.domain.AccountRepository
import mu.KLogger
import mu.KotlinLogging

class UpdateAccountLoansCommandHandler(
    accountRepository: AccountRepository,
) : CommandHandler<UpdateAccountLoansCommand> {
    override val logger: KLogger = KotlinLogging.logger {}
    private val accountLoansUpdater = AccountLoansUpdater(accountRepository)

    override fun handle(command: UpdateAccountLoansCommand): Unit =
        accountLoansUpdater.invoke(
            accountId = command.accountId,
            type =
                when (command) {
                    is UpdateAccountLoansCommand.IncreaseAccountLoansCommand -> AccountLoansUpdater.UpdateType.INCREASE
                    is UpdateAccountLoansCommand.DecreaseAccountLoansCommand -> AccountLoansUpdater.UpdateType.DECREASE
                },
        ).getOrThrow()
}

sealed class UpdateAccountLoansCommand(open val accountId: AccountId) : Command {
    data class IncreaseAccountLoansCommand(override val accountId: AccountId) : UpdateAccountLoansCommand(accountId)

    data class DecreaseAccountLoansCommand(override val accountId: AccountId) : UpdateAccountLoansCommand(accountId)
}
