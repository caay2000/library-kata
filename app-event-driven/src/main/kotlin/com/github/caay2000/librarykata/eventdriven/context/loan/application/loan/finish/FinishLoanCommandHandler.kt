package com.github.caay2000.librarykata.eventdriven.context.loan.application.loan.finish

import com.github.caay2000.common.arrow.getOrThrow
import com.github.caay2000.common.cqrs.Command
import com.github.caay2000.common.cqrs.CommandHandler
import com.github.caay2000.common.event.DomainEventPublisher
import com.github.caay2000.librarykata.eventdriven.context.loan.application.LoanRepository
import com.github.caay2000.librarykata.eventdriven.context.loan.domain.BookId
import com.github.caay2000.librarykata.eventdriven.context.loan.domain.FinishedAt
import mu.KLogger
import mu.KotlinLogging
import java.time.LocalDateTime
import java.util.UUID

class FinishLoanCommandHandler(
    loanRepository: LoanRepository,
    eventPublisher: DomainEventPublisher,
) : CommandHandler<FinishLoanCommand> {
    override val logger: KLogger = KotlinLogging.logger {}
    private val finisher = LoanFinisher(loanRepository, eventPublisher)

    override fun handle(command: FinishLoanCommand): Unit = finisher.invoke(bookId = BookId(command.bookId), finishedAt = FinishedAt(command.finishedAt)).getOrThrow()
}

data class FinishLoanCommand(
    val bookId: UUID,
    val finishedAt: LocalDateTime,
) : Command
