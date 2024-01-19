package com.github.caay2000.librarykata.event.context.loan.primaryadapter.event

import com.github.caay2000.common.event.DomainEventSubscriber
import com.github.caay2000.librarykata.event.context.loan.application.UserRepository
import com.github.caay2000.librarykata.event.context.loan.application.user.update.IncreaseUserCurrentLoansCommand
import com.github.caay2000.librarykata.event.context.loan.application.user.update.UpdateUserCurrentLoansCommandHandler
import com.github.caay2000.librarykata.event.events.loan.LoanCreatedEvent
import mu.KLogger
import mu.KotlinLogging

class IncreaseUserCurrentLoansOnLoanCreatedEventSubscriber(userRepository: UserRepository) : DomainEventSubscriber<LoanCreatedEvent>() {
    override val logger: KLogger = KotlinLogging.logger {}
    private val commandHandler = UpdateUserCurrentLoansCommandHandler(userRepository)

    override fun handleEvent(event: LoanCreatedEvent) {
        commandHandler.invoke(IncreaseUserCurrentLoansCommand(event.userId))
    }
}
