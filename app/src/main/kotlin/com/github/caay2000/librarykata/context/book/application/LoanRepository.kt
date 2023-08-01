package com.github.caay2000.librarykata.context.book.application

import arrow.core.Either
import com.github.caay2000.common.database.RepositoryError
import com.github.caay2000.librarykata.context.book.domain.Loan
import com.github.caay2000.librarykata.context.book.domain.LoanId
import com.github.caay2000.librarykata.context.book.domain.UserId

interface LoanRepository {

    fun save(loan: Loan): Either<RepositoryError, Unit>
    fun searchByUserId(userId: UserId): Either<RepositoryError, List<Loan>>

    fun findById(id: LoanId): Either<RepositoryError, Loan>
}
