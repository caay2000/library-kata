package com.github.caay2000.librarykata.eventdriven.context.loan.application.find

import com.github.caay2000.common.arrow.getOrThrow
import com.github.caay2000.common.cqrs.Query
import com.github.caay2000.common.cqrs.QueryHandler
import com.github.caay2000.common.cqrs.QueryResponse
import com.github.caay2000.librarykata.eventdriven.context.loan.domain.Loan
import com.github.caay2000.librarykata.eventdriven.context.loan.domain.LoanId
import com.github.caay2000.librarykata.eventdriven.context.loan.domain.LoanRepository
import mu.KLogger
import mu.KotlinLogging

class FindLoanQueryHandler(loanRepository: LoanRepository) : QueryHandler<FindLoanQuery, FindLoanQueryResponse> {
    override val logger: KLogger = KotlinLogging.logger {}

    private val finder = LoanFinder(loanRepository)

    override fun handle(query: FindLoanQuery): FindLoanQueryResponse =
        finder.invoke(query.id)
            .map { loan -> FindLoanQueryResponse(loan) }
            .getOrThrow()
}

data class FindLoanQuery(val id: LoanId) : Query

data class FindLoanQueryResponse(val loan: Loan) : QueryResponse
