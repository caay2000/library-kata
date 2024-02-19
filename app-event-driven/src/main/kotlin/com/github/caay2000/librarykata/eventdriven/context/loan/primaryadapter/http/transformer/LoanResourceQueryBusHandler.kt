package com.github.caay2000.librarykata.eventdriven.context.loan.primaryadapter.http.transformer

import com.github.caay2000.common.query.ResourceQuery
import com.github.caay2000.common.query.ResourceQueryHandler
import com.github.caay2000.common.query.ResourceQueryResponse
import com.github.caay2000.librarykata.eventdriven.context.loan.application.find.FindLoanQuery
import com.github.caay2000.librarykata.eventdriven.context.loan.application.find.FindLoanQueryHandler
import com.github.caay2000.librarykata.eventdriven.context.loan.domain.Loan
import com.github.caay2000.librarykata.eventdriven.context.loan.domain.LoanId
import com.github.caay2000.librarykata.eventdriven.context.loan.domain.LoanRepository
import com.github.caay2000.librarykata.jsonapi.context.loan.LoanResource
import mu.KLogger
import mu.KotlinLogging

class LoanResourceQueryBusHandler(loanRepository: LoanRepository) : ResourceQueryHandler {
    private val loanQueryHandler = FindLoanQueryHandler(loanRepository)
    private val loanResourceTransformer = LoanResourceTransformer()

    override val logger: KLogger = KotlinLogging.logger {}
    override val type: String = LoanResource.TYPE

    override fun handle(query: ResourceQuery): ResourceQueryResponse {
        val account = retrieveLoan(query)
        return ResourceQueryResponse(loanResourceTransformer.invoke(account))
    }

    private fun retrieveLoan(query: ResourceQuery): Loan = loanQueryHandler.invoke(FindLoanQuery(LoanId(query.identifier))).loan
}
