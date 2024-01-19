package com.github.caay2000.librarykata.event.context.loan.primaryadapter.event

import com.github.caay2000.common.event.DomainEventSubscriber
import com.github.caay2000.librarykata.event.context.loan.application.UserRepository
import com.github.caay2000.librarykata.event.context.loan.application.user.update.DecreaseUserCurrentLoansCommand
import com.github.caay2000.librarykata.event.context.loan.application.user.update.UpdateUserCurrentLoansCommandHandler
import com.github.caay2000.librarykata.event.events.loan.LoanFinishedEvent
import mu.KLogger
import mu.KotlinLogging

class DecreaseUserCurrentLoansOnLoanFinishedEventSubscriber(userRepository: UserRepository) : DomainEventSubscriber<LoanFinishedEvent>() {
    override val logger: KLogger = KotlinLogging.logger {}
    private val commandHandler = UpdateUserCurrentLoansCommandHandler(userRepository)

    override fun handleEvent(event: LoanFinishedEvent) {
        commandHandler.invoke(DecreaseUserCurrentLoansCommand(event.userId))
    }
}
