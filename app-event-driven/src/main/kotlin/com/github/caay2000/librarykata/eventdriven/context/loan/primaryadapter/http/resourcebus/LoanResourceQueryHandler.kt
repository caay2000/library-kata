package com.github.caay2000.librarykata.eventdriven.context.loan.primaryadapter.http.resourcebus

import com.github.caay2000.common.resourcebus.ResourceBusQueryHandler
import com.github.caay2000.librarykata.eventdriven.context.loan.application.account.find.FindAccountQuery
import com.github.caay2000.librarykata.eventdriven.context.loan.application.account.find.FindAccountQueryHandler
import com.github.caay2000.librarykata.eventdriven.context.loan.application.book.find.FindBookQuery
import com.github.caay2000.librarykata.eventdriven.context.loan.application.book.find.FindBookQueryHandler
import com.github.caay2000.librarykata.eventdriven.context.loan.application.find.FindLoanQuery
import com.github.caay2000.librarykata.eventdriven.context.loan.application.find.FindLoanQueryHandler
import com.github.caay2000.librarykata.eventdriven.context.loan.domain.Account
import com.github.caay2000.librarykata.eventdriven.context.loan.domain.AccountRepository
import com.github.caay2000.librarykata.eventdriven.context.loan.domain.Book
import com.github.caay2000.librarykata.eventdriven.context.loan.domain.BookRepository
import com.github.caay2000.librarykata.eventdriven.context.loan.domain.LoanId
import com.github.caay2000.librarykata.eventdriven.context.loan.domain.LoanRepository
import com.github.caay2000.librarykata.eventdriven.context.loan.primaryadapter.http.transformer.toJsonApiLoanResource
import com.github.caay2000.librarykata.jsonapi.context.loan.LoanResource

class LoanResourceQueryHandler(
    loanRepository: LoanRepository,
    accountRepository: AccountRepository,
    bookRepository: BookRepository,
) : ResourceBusQueryHandler<LoanResource> {
    private val loanQueryHandler = FindLoanQueryHandler(loanRepository)
    private val accountQueryHandler = FindAccountQueryHandler(accountRepository)
    private val bookQueryHandler = FindBookQueryHandler(bookRepository)

    override fun retrieve(
        identifier: String,
        includeRelationships: Boolean,
    ): LoanResource {
        val loan = loanQueryHandler.invoke(FindLoanQuery(LoanId(identifier))).loan

        val accountRelationship: Account? =
            if (includeRelationships) {
                accountQueryHandler.invoke(FindAccountQuery(loan.accountId)).account
            } else {
                null
            }

        val bookRelationship: Book? =
            if (includeRelationships) {
                bookQueryHandler.invoke(FindBookQuery(loan.bookId)).book
            } else {
                null
            }

        return loan.toJsonApiLoanResource(accountRelationship, bookRelationship)
    }
}
