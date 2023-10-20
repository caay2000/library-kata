package com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.loan

import com.github.caay2000.common.dateprovider.DateProvider
import com.github.caay2000.common.http.Controller
import com.github.caay2000.common.idgenerator.IdGenerator
import com.github.caay2000.common.jsonapi.JsonApiRequestDocument
import com.github.caay2000.common.jsonapi.context.loan.LoanRequestResource
import com.github.caay2000.librarykata.hexagonal.context.application.account.AccountRepository
import com.github.caay2000.librarykata.hexagonal.context.application.book.BookRepository
import com.github.caay2000.librarykata.hexagonal.context.application.loan.LoanRepository
import com.github.caay2000.librarykata.hexagonal.context.application.loan.create.CreateLoanCommand
import com.github.caay2000.librarykata.hexagonal.context.application.loan.create.CreateLoanCommandHandler
import com.github.caay2000.librarykata.hexagonal.context.application.loan.find.FindLoanByIdQuery
import com.github.caay2000.librarykata.hexagonal.context.application.loan.find.FindLoanByIdQueryHandler
import com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.serialization.toJsonApiDocument
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
    accountRepository: AccountRepository,
    loanRepository: LoanRepository,
) : Controller {

    override val logger: KLogger = KotlinLogging.logger {}

    private val commandHandler = CreateLoanCommandHandler(bookRepository, accountRepository, loanRepository)
    private val loanQueryHandler = FindLoanByIdQueryHandler(loanRepository)

    override suspend fun handle(call: ApplicationCall) {
        val request = call.receive<JsonApiRequestDocument<LoanRequestResource>>()

        val loanId = idGenerator.generate()
        val datetime = dateProvider.dateTime()
        commandHandler.invoke(request.toCreateLoanCommand(loanId, datetime))

        val loanQueryResponse = loanQueryHandler.invoke(FindLoanByIdQuery(UUID.fromString(loanId)))
        call.respond(HttpStatusCode.Created, loanQueryResponse.loan.toJsonApiDocument())
    }

    private fun JsonApiRequestDocument<LoanRequestResource>.toCreateLoanCommand(loanId: String, datetime: LocalDateTime) =
        CreateLoanCommand(
            loanId = UUID.fromString(loanId),
            accountId = UUID.fromString(data.attributes.accountId),
            bookIsbn = data.attributes.bookIsbn,
            createdAt = datetime,
        )
}
