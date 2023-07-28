package com.github.caay2000.librarykata.context.loan.primaryadapter.http

import com.github.caay2000.common.dateprovider.DateProvider
import com.github.caay2000.common.event.DomainEventPublisher
import com.github.caay2000.common.http.Controller
import com.github.caay2000.common.idgenerator.IdGenerator
import com.github.caay2000.librarykata.context.loan.application.BookRepository
import com.github.caay2000.librarykata.context.loan.application.LoanRepository
import com.github.caay2000.librarykata.context.loan.application.UserRepository
import com.github.caay2000.librarykata.context.loan.application.loan.create.CreateLoanCommand
import com.github.caay2000.librarykata.context.loan.application.loan.create.CreateLoanCommandHandler
import com.github.caay2000.librarykata.context.loan.application.loan.find.FindLoanByIdQuery
import com.github.caay2000.librarykata.context.loan.application.loan.find.FindLoanByIdQueryHandler
import com.github.caay2000.librarykata.context.loan.primaryadapter.http.serialization.LoanRequestDocument
import com.github.caay2000.librarykata.context.loan.primaryadapter.http.serialization.toLoanDocument
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import mu.KLogger
import mu.KotlinLogging
import java.time.LocalDateTime
import java.util.UUID

class CreateLoanController(
    private val idGenerator: IdGenerator,
    private val dateProvider: DateProvider,
    bookRepository: BookRepository,
    userRepository: UserRepository,
    loanRepository: LoanRepository,
    eventPublisher: DomainEventPublisher,
) : Controller {

    override val logger: KLogger = KotlinLogging.logger {}

    private val commandHandler = CreateLoanCommandHandler(bookRepository, userRepository, loanRepository, eventPublisher)
    private val queryHandler = FindLoanByIdQueryHandler(loanRepository)

    override suspend fun handle(call: ApplicationCall) {
        val request = call.receive<LoanRequestDocument>()

        val loanId = UUID.fromString(idGenerator.generate())
        val datetime = dateProvider.dateTime()
        commandHandler.invoke(request.toCreateLoanCommand(loanId, datetime))

        val queryResponse = queryHandler.handle(FindLoanByIdQuery(loanId))
        call.respond(HttpStatusCode.Created, queryResponse.loan.toLoanDocument())
    }

    private fun LoanRequestDocument.toCreateLoanCommand(loanId: UUID, datetime: LocalDateTime) =
        CreateLoanCommand(
            loanId = loanId,
            userId = userId,
            bookIsbn = bookIsbn,
            createdAt = datetime,
        )
}
