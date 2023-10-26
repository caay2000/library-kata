package com.github.caay2000.librarykata.hexagonal.context.application.loan.find

import com.github.caay2000.common.arrow.getOrThrow
import com.github.caay2000.common.cqrs.Query
import com.github.caay2000.common.cqrs.QueryHandler
import com.github.caay2000.common.cqrs.QueryResponse
import com.github.caay2000.librarykata.hexagonal.context.domain.loan.Loan
import com.github.caay2000.librarykata.hexagonal.context.domain.loan.LoanId
import com.github.caay2000.librarykata.hexagonal.context.domain.loan.LoanRepository
import mu.KLogger
import mu.KotlinLogging

class FindLoanByIdQueryHandler(loanRepository: LoanRepository) : QueryHandler<FindLoanByIdQuery, FindLoanByIdQueryResponse> {

    override val logger: KLogger = KotlinLogging.logger {}

    private val finder = LoanFinder(loanRepository)

    override fun handle(query: FindLoanByIdQuery): FindLoanByIdQueryResponse =
        finder.invoke(query.id)
            .map { loan -> FindLoanByIdQueryResponse(loan) }
            .getOrThrow()
}

data class FindLoanByIdQuery(val id: LoanId) : Query
data class FindLoanByIdQueryResponse(val loan: Loan) : QueryResponse
