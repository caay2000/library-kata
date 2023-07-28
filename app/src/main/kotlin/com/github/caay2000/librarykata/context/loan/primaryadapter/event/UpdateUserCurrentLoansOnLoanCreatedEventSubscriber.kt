package com.github.caay2000.librarykata.context.loan.primaryadapter.event

import com.github.caay2000.common.event.DomainEventSubscriber
import com.github.caay2000.librarykata.context.loan.application.UserRepository
import com.github.caay2000.librarykata.context.loan.application.user.update.UpdateUserCurrentLoansCommand
import com.github.caay2000.librarykata.context.loan.application.user.update.UpdateUserCurrentLoansCommandHandler
import com.github.caay2000.librarykata.events.loan.LoanCreatedEvent
import mu.KLogger
import mu.KotlinLogging

class UpdateUserCurrentLoansOnLoanCreatedEventSubscriber(userRepository: UserRepository) : DomainEventSubscriber<LoanCreatedEvent>() {

    override val logger: KLogger = KotlinLogging.logger {}
    private val commandHandler = UpdateUserCurrentLoansCommandHandler(userRepository)

    override fun handleEvent(event: LoanCreatedEvent) {
        commandHandler.invoke(UpdateUserCurrentLoansCommand(event.userId, 1))
    }
}
