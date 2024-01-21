package com.github.caay2000.librarykata.eventdriven.context.account.domain

data class Loan(
    val accountId: AccountId,
    val loanId: LoanId,
    val bookId: BookId,
)

@JvmInline
value class LoanId(val value: String)

@JvmInline
value class BookId(val value: String)
