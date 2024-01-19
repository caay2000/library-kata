package com.github.caay2000.librarykata.eventdriven.context.loan.application.loan.create

import com.github.caay2000.common.arrow.getOrThrow
import com.github.caay2000.common.cqrs.Command
import com.github.caay2000.common.cqrs.CommandHandler
import com.github.caay2000.common.event.DomainEventPublisher
import com.github.caay2000.librarykata.eventdriven.context.loan.application.BookRepository
import com.github.caay2000.librarykata.eventdriven.context.loan.application.LoanRepository
import com.github.caay2000.librarykata.eventdriven.context.loan.application.UserRepository
import com.github.caay2000.librarykata.eventdriven.context.loan.domain.BookIsbn
import com.github.caay2000.librarykata.eventdriven.context.loan.domain.CreatedAt
import com.github.caay2000.librarykata.eventdriven.context.loan.domain.LoanId
import com.github.caay2000.librarykata.eventdriven.context.loan.domain.UserId
import mu.KLogger
import mu.KotlinLogging
import java.time.LocalDateTime
import java.util.UUID

class CreateLoanCommandHandler(
    bookRepository: BookRepository,
    userRepository: UserRepository,
    loanRepository: LoanRepository,
    eventPublisher: DomainEventPublisher,
) : CommandHandler<CreateLoanCommand> {
    override val logger: KLogger = KotlinLogging.logger {}
    private val creator = LoanCreator(bookRepository, userRepository, loanRepository, eventPublisher)

    override fun handle(command: CreateLoanCommand): Unit =
        creator.invoke(
            loanId = LoanId(command.loanId),
            userId = UserId(command.userId),
            bookIsbn = BookIsbn(command.bookIsbn),
            createdAt = CreatedAt(command.createdAt),
        ).getOrThrow()
}

data class CreateLoanCommand(
    val loanId: UUID,
    val userId: UUID,
    val bookIsbn: String,
    val createdAt: LocalDateTime,
) : Command
