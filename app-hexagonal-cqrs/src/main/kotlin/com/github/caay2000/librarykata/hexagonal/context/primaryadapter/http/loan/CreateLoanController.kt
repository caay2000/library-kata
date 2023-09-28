package com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.loan

import com.github.caay2000.common.dateprovider.DateProvider
import com.github.caay2000.common.http.Controller
import com.github.caay2000.common.idgenerator.IdGenerator
import com.github.caay2000.librarykata.hexagonal.context.application.account.AccountRepository
import com.github.caay2000.librarykata.hexagonal.context.application.book.BookRepository
import com.github.caay2000.librarykata.hexagonal.context.application.loan.LoanRepository
import com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.serialization.LoanRequestDocument
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receive
import mu.KLogger
import mu.KotlinLogging

class CreateLoanController(
    private val idGenerator: IdGenerator,
    private val dateProvider: DateProvider,
    bookRepository: BookRepository,
    accountRepository: AccountRepository,
    loanRepository: LoanRepository,
) : Controller {

    override val logger: KLogger = KotlinLogging.logger {}

//    private val commandHandler = CreateLoanCommandHandler(bookRepository, accountRepository, loanRepository)
//    private val queryHandler = FindLoanByIdQueryHandler(loanRepository)

    override suspend fun handle(call: ApplicationCall) {
        val request = call.receive<LoanRequestDocument>()

        val loanId = idGenerator.generate()
        val datetime = dateProvider.dateTime()
//        commandHandler.invoke(request.toCreateLoanCommand(loanId, datetime))
//
//        val queryResponse = queryHandler.invoke(FindLoanByIdQuery(loanId))
//        call.respond(HttpStatusCode.Created, queryResponse.loan.toLoanDocument())
    }

//    private fun LoanRequestDocument.toCreateLoanCommand(loanId: String, datetime: LocalDateTime) =
//        CreateLoanCommand(
//            loanId = loanId,
//            userId = userId.toString(),
//            bookIsbn = bookIsbn,
//            createdAt = datetime,
//        )
}
