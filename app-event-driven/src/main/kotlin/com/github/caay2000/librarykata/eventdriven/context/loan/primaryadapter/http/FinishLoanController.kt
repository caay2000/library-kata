package com.github.caay2000.librarykata.eventdriven.context.loan.primaryadapter.http

import com.github.caay2000.common.dateprovider.DateProvider
import com.github.caay2000.common.event.DomainEventPublisher
import com.github.caay2000.common.http.Controller
import com.github.caay2000.librarykata.eventdriven.context.loan.application.LoanRepository
import com.github.caay2000.librarykata.eventdriven.context.loan.application.loan.finish.FinishLoanCommand
import com.github.caay2000.librarykata.eventdriven.context.loan.application.loan.finish.FinishLoanCommandHandler
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.respond
import mu.KLogger
import mu.KotlinLogging
import java.util.UUID

class FinishLoanController(
    private val dateProvider: DateProvider,
    loanRepository: LoanRepository,
    eventPublisher: DomainEventPublisher,
) : Controller {

    override val logger: KLogger = KotlinLogging.logger {}

    private val commandHandler = FinishLoanCommandHandler(loanRepository, eventPublisher)

    override suspend fun handle(call: ApplicationCall) {
        val bookId = UUID.fromString(call.parameters["bookId"])

        val datetime = dateProvider.dateTime()
        commandHandler.invoke(FinishLoanCommand(bookId = bookId, finishedAt = datetime))

        call.respond(HttpStatusCode.Accepted)
    }
}
