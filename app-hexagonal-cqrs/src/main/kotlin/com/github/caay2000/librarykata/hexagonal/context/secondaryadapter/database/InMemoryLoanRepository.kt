package com.github.caay2000.librarykata.hexagonal.context.secondaryadapter.database

import arrow.core.Either
import com.github.caay2000.common.database.RepositoryError
import com.github.caay2000.librarykata.hexagonal.context.domain.book.Book
import com.github.caay2000.librarykata.hexagonal.context.domain.loan.FindLoanCriteria
import com.github.caay2000.librarykata.hexagonal.context.domain.loan.Loan
import com.github.caay2000.librarykata.hexagonal.context.domain.loan.LoanRepository
import com.github.caay2000.librarykata.hexagonal.context.domain.loan.SearchLoanCriteria
import com.github.caay2000.memorydb.InMemoryDatasource

class InMemoryLoanRepository(private val datasource: InMemoryDatasource) : LoanRepository {
    override fun save(loan: Loan) {
        datasource.save(TABLE_NAME, loan.id.toString(), loan)
    }

    override fun find(criteria: FindLoanCriteria): Either<RepositoryError, Loan> =
        Either.catch {
            when (criteria) {
                is FindLoanCriteria.ById -> datasource.getById<Loan>(TABLE_NAME, criteria.id.toString())!!
                is FindLoanCriteria.ByBookIdAndNotFinished -> datasource.getAll<Loan>(TABLE_NAME).filter { it.bookId == criteria.bookId }.first { it.isNotFinished }
            }
        }.mapLeft { error ->
            when (error) {
                is NullPointerException -> RepositoryError.NotFoundError()
                is NoSuchElementException -> RepositoryError.NotFoundError()
                else -> throw error
            }
        }

    override fun search(criteria: SearchLoanCriteria): List<Loan> =
        when (criteria) {
            is SearchLoanCriteria.ByAccountId -> datasource.getAll<Loan>(TABLE_NAME).filter { it.accountId == criteria.accountId }
            is SearchLoanCriteria.ByBookId -> datasource.getAll<Loan>(TABLE_NAME).filter { it.bookId == criteria.bookId }
            is SearchLoanCriteria.ByBookIsbn -> {
                val bookIds = datasource.getAll<Book>(BOOK_TABLE_NAME).filter { it.isbn == criteria.bookIsbn }.map { it.id }
                datasource.getAll<Loan>(TABLE_NAME).filter { bookIds.contains(it.bookId) }
            }
        }

    companion object {
        private const val TABLE_NAME = "loan"

        // TODO having this table here is like the loan repository is doing a join with book table
        private const val BOOK_TABLE_NAME = "book"
    }
}
