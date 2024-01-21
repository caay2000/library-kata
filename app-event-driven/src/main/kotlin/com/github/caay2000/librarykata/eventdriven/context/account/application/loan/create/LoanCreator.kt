package com.github.caay2000.librarykata.eventdriven.context.account.application.loan.create

import com.github.caay2000.librarykata.eventdriven.context.account.domain.AccountId
import com.github.caay2000.librarykata.eventdriven.context.account.domain.BookId
import com.github.caay2000.librarykata.eventdriven.context.account.domain.Loan
import com.github.caay2000.librarykata.eventdriven.context.account.domain.LoanId
import com.github.caay2000.librarykata.eventdriven.context.account.domain.LoanRepository

class LoanCreator(private val loanRepository: LoanRepository) {
    fun invoke(
        loanId: LoanId,
        accountId: AccountId,
        bookId: BookId,
    ) = loanRepository.save(Loan(loanId, accountId, bookId))
}
