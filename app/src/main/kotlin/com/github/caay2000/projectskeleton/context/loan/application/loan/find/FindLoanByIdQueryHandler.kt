package com.github.caay2000.projectskeleton.context.loan.application.loan.find

import com.github.caay2000.common.arrow.getOrThrow
import com.github.caay2000.projectskeleton.common.cqrs.Query
import com.github.caay2000.projectskeleton.common.cqrs.QueryHandler
import com.github.caay2000.projectskeleton.common.cqrs.QueryResponse
import com.github.caay2000.projectskeleton.context.loan.application.LoanRepository
import com.github.caay2000.projectskeleton.context.loan.domain.Loan
import com.github.caay2000.projectskeleton.context.loan.domain.LoanId
import java.util.UUID

class FindLoanByIdQueryHandler(loanRepository: LoanRepository) : QueryHandler<FindLoanByIdQuery, FindLoanByIdQueryResponse> {

    private val finder = LoanFinder(loanRepository)

    override fun handle(query: FindLoanByIdQuery): FindLoanByIdQueryResponse =
        finder.invoke(LoanId(query.id))
            .map { loan -> FindLoanByIdQueryResponse(loan) }
            .getOrThrow()
}

data class FindLoanByIdQuery(val id: UUID) : Query
data class FindLoanByIdQueryResponse(val loan: Loan) : QueryResponse
