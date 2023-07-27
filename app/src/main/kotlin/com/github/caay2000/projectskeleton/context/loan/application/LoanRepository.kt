package com.github.caay2000.projectskeleton.context.loan.application

import arrow.core.Either
import com.github.caay2000.common.database.RepositoryError
import com.github.caay2000.projectskeleton.context.loan.domain.Loan
import com.github.caay2000.projectskeleton.context.loan.domain.LoanId

interface LoanRepository {

    fun save(loan: Loan): Either<RepositoryError, Unit>
    fun findById(id: LoanId): Either<RepositoryError, Loan>
}
