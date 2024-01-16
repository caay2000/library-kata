package com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.loan

import com.github.caay2000.common.http.Controller
import com.github.caay2000.librarykata.hexagonal.context.application.loan.find.FindLoanByIdQuery
import com.github.caay2000.librarykata.hexagonal.context.application.loan.find.FindLoanByIdQueryHandler
import com.github.caay2000.librarykata.hexagonal.context.domain.loan.LoanId
import com.github.caay2000.librarykata.hexagonal.context.domain.loan.LoanRepository
import com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.serialization.toJsonApiDocument
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.respond
import mu.KLogger
import mu.KotlinLogging
import java.util.UUID

class FindLoanController(
    loanRepository: LoanRepository,
) : Controller {
    override val logger: KLogger = KotlinLogging.logger {}

    private val queryHandler = FindLoanByIdQueryHandler(loanRepository)

    override suspend fun handle(call: ApplicationCall) {
        val loanId = UUID.fromString(call.parameters["loanId"]!!)

        val queryResult = queryHandler.invoke(FindLoanByIdQuery(LoanId(loanId.toString())))

        call.respond(HttpStatusCode.OK, queryResult.loan.toJsonApiDocument())
    }
}
