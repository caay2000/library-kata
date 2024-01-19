package com.github.caay2000.librarykata.event.context.loan.application.loan.find

import com.github.caay2000.common.arrow.getOrThrow
import com.github.caay2000.common.cqrs.Query
import com.github.caay2000.common.cqrs.QueryHandler
import com.github.caay2000.common.cqrs.QueryResponse
import com.github.caay2000.librarykata.event.context.loan.application.LoanRepository
import com.github.caay2000.librarykata.event.context.loan.domain.Loan
import com.github.caay2000.librarykata.event.context.loan.domain.LoanId
import mu.KLogger
import mu.KotlinLogging
import java.util.UUID

class FindLoanByIdQueryHandler(loanRepository: LoanRepository) : QueryHandler<FindLoanByIdQuery, FindLoanByIdQueryResponse> {
    override val logger: KLogger = KotlinLogging.logger {}

    private val finder = LoanFinder(loanRepository)

    override fun handle(query: FindLoanByIdQuery): FindLoanByIdQueryResponse =
        finder.invoke(LoanId(query.id))
            .map { loan -> FindLoanByIdQueryResponse(loan) }
            .getOrThrow()
}

data class FindLoanByIdQuery(val id: UUID) : Query

data class FindLoanByIdQueryResponse(val loan: Loan) : QueryResponse
