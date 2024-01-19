package com.github.caay2000.librarykata.hexagonal.context.domain.loan

import arrow.core.Either
import com.github.caay2000.common.database.RepositoryError
import com.github.caay2000.librarykata.hexagonal.context.domain.account.AccountId
import com.github.caay2000.librarykata.hexagonal.context.domain.book.BookId
import com.github.caay2000.librarykata.hexagonal.context.domain.book.BookIsbn

interface LoanRepository {
    fun save(loan: Loan)

    fun find(criteria: FindLoanCriteria): Either<RepositoryError, Loan>

    fun search(criteria: SearchLoanCriteria): List<Loan>
}

sealed class FindLoanCriteria {
    class ById(val id: LoanId) : FindLoanCriteria()

    class ByBookIdAndNotFinished(val bookId: BookId) : FindLoanCriteria()
}

sealed class SearchLoanCriteria {
    class ByAccountId(val accountId: AccountId) : SearchLoanCriteria()

    class ByBookId(val bookId: BookId) : SearchLoanCriteria()

    class ByBookIsbn(val bookIsbn: BookIsbn) : SearchLoanCriteria()
}

fun <E> LoanRepository.saveOrElse(
    loan: Loan,
    onError: (Throwable) -> E = { throw it },
): Either<E, Loan> =
    Either.catch { save(loan) }
        .mapLeft { onError(it) }.map { loan }

fun <E> LoanRepository.findOrElse(
    criteria: FindLoanCriteria,
    onResourceDoesNotExist: (Throwable) -> E = { throw it },
    onUnexpectedError: (Throwable) -> E = { throw it },
): Either<E, Loan> =
    find(criteria)
        .mapLeft { error ->
            if (error is RepositoryError.NotFoundError) {
                onResourceDoesNotExist(error)
            } else {
                onUnexpectedError(error)
            }
        }
