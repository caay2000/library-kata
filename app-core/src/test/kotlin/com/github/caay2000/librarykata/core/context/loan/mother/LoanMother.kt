package com.github.caay2000.librarykata.core.context.loan.mother

import com.github.caay2000.librarykata.hexagonal.context.account.domain.AccountId
import com.github.caay2000.librarykata.hexagonal.context.book.domain.BookId
import com.github.caay2000.librarykata.hexagonal.context.loan.domain.CreatedAt
import com.github.caay2000.librarykata.hexagonal.context.loan.domain.FinishedAt
import com.github.caay2000.librarykata.hexagonal.context.loan.domain.Loan
import com.github.caay2000.librarykata.hexagonal.context.loan.domain.LoanId
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

    fun finishedLoan(finishedAt: FinishedAt = FinishedAt(LocalDateTime.now())) = random(createdAt = CreatedAt(finishedAt.value.minusDays(3)), finishedAt = finishedAt)
}
