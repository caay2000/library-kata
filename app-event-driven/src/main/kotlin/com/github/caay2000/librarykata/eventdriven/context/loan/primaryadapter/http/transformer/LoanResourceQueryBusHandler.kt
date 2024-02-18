package com.github.caay2000.librarykata.eventdriven.context.loan.primaryadapter.http.transformer

import com.github.caay2000.common.jsonapi.JsonApiResource
import com.github.caay2000.common.querybus.Query
import com.github.caay2000.common.querybus.QueryBusHandler
import com.github.caay2000.common.querybus.QueryResponse
import com.github.caay2000.librarykata.eventdriven.context.loan.application.find.FindLoanQuery
import com.github.caay2000.librarykata.eventdriven.context.loan.application.find.FindLoanQueryHandler
import com.github.caay2000.librarykata.eventdriven.context.loan.domain.Loan
import com.github.caay2000.librarykata.eventdriven.context.loan.domain.LoanId
import com.github.caay2000.librarykata.eventdriven.context.loan.domain.LoanRepository
import mu.KLogger
import mu.KotlinLogging

class LoanResourceQueryBusHandler(loanRepository: LoanRepository) : QueryBusHandler<LoanResourceQuery, LoanResourceQueryResponse> {
    private val loanQueryHandler = FindLoanQueryHandler(loanRepository)
    private val loanResourceTransformer = LoanResourceTransformer()

    override val logger: KLogger = KotlinLogging.logger {}

    override fun handle(query: LoanResourceQuery): LoanResourceQueryResponse {
        val account = retrieveLoan(query)
        return LoanResourceQueryResponse(loanResourceTransformer.invoke(account))
    }

    private fun retrieveLoan(query: LoanResourceQuery): Loan =
        when (query) {
            is LoanResourceQuery.ByIdentifier -> loanQueryHandler.invoke(FindLoanQuery(LoanId(query.identifier))).loan
            is LoanResourceQuery.ByValue -> query.value
        }
}

sealed class LoanResourceQuery : Query {
    data class ByIdentifier(val identifier: String) : LoanResourceQuery()

    data class ByValue(val value: Loan) : LoanResourceQuery()
}

data class LoanResourceQueryResponse(val resource: JsonApiResource) : QueryResponse
