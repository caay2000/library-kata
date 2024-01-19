package com.github.caay2000.librarykata.eventdriven.context.account.application.loan.start

import com.github.caay2000.librarykata.eventdriven.context.account.application.LoanRepository
import com.github.caay2000.librarykata.eventdriven.context.account.domain.AccountId
import com.github.caay2000.librarykata.eventdriven.context.account.domain.BookId
import com.github.caay2000.librarykata.eventdriven.context.account.domain.Loan
import com.github.caay2000.librarykata.eventdriven.context.account.domain.LoanId
import com.github.caay2000.librarykata.eventdriven.context.account.domain.StartLoanDateTime

class LoanStarter(private val loanRepository: LoanRepository) {
    fun invoke(
        id: LoanId,
        accountId: AccountId,
        bookId: BookId,
        startedAt: StartLoanDateTime,
    ) {
        startLoan(id, accountId, bookId, startedAt).save()
    }

    private fun startLoan(
        id: LoanId,
        accountId: AccountId,
        bookId: BookId,
        startedAt: StartLoanDateTime,
    ): Loan = Loan(id, accountId, bookId, startedAt)

    private fun Loan.save() {
        loanRepository.save(this)
    }
}
