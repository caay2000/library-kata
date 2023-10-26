package com.github.caay2000.librarykata.hexagonal.context.loan.mother

import com.github.caay2000.librarykata.hexagonal.context.domain.account.AccountId
import com.github.caay2000.librarykata.hexagonal.context.domain.book.BookId
import com.github.caay2000.librarykata.hexagonal.context.domain.loan.CreatedAt
import com.github.caay2000.librarykata.hexagonal.context.domain.loan.FinishedAt
import com.github.caay2000.librarykata.hexagonal.context.domain.loan.Loan
import com.github.caay2000.librarykata.hexagonal.context.domain.loan.LoanId
import java.time.LocalDateTime
import java.util.UUID

object LoanMother {

    fun random(
        id: LoanId = LoanIdMother.random(),
        bookId: BookId = BookId(UUID.randomUUID().toString()),
        accountId: AccountId = AccountId(UUID.randomUUID().toString()),
        createdAt: CreatedAt = CreatedAt(LocalDateTime.now()),
        finishedAt: FinishedAt? = null,
    ) = Loan(
        id,
        bookId,
        accountId,
        createdAt,
        finishedAt,
    )

    fun finishedLoan(
        finishedAt: FinishedAt = FinishedAt(LocalDateTime.now()),
    ) = random(createdAt = CreatedAt(finishedAt.value.minusDays(3)), finishedAt = finishedAt)
}
