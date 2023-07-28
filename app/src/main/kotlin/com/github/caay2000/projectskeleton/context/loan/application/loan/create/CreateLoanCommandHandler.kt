package com.github.caay2000.projectskeleton.context.loan.application.loan.create

import com.github.caay2000.common.arrow.getOrThrow
import com.github.caay2000.common.event.DomainEventPublisher
import com.github.caay2000.projectskeleton.common.cqrs.Command
import com.github.caay2000.projectskeleton.common.cqrs.CommandHandler
import com.github.caay2000.projectskeleton.context.loan.application.BookRepository
import com.github.caay2000.projectskeleton.context.loan.application.LoanRepository
import com.github.caay2000.projectskeleton.context.loan.application.UserRepository
import com.github.caay2000.projectskeleton.context.loan.domain.BookId
import com.github.caay2000.projectskeleton.context.loan.domain.CreatedAt
import com.github.caay2000.projectskeleton.context.loan.domain.LoanId
import com.github.caay2000.projectskeleton.context.loan.domain.UserId
import java.time.LocalDateTime
import java.util.UUID

class CreateLoanCommandHandler(
    bookRepository: BookRepository,
    userRepository: UserRepository,
    loanRepository: LoanRepository,
    eventPublisher: DomainEventPublisher,
) : CommandHandler<CreateLoanCommand> {

    private val creator = LoanCreator(bookRepository, userRepository, loanRepository, eventPublisher)

    override fun invoke(command: CreateLoanCommand): Unit =
        creator.invoke(
            loanId = LoanId(command.loanId),
            userId = UserId(command.userId),
            bookId = BookId(command.bookId),
            createdAt = CreatedAt(command.createdAt),
        ).getOrThrow()
}

data class CreateLoanCommand(
    val loanId: UUID,
    val userId: UUID,
    val bookId: UUID,
    val createdAt: LocalDateTime,
) : Command
