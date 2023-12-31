package com.github.caay2000.librarykata.hexagonal.context.loan.mother

import com.github.caay2000.librarykata.hexagonal.context.domain.AccountId
import com.github.caay2000.librarykata.hexagonal.context.domain.BookId
import com.github.caay2000.librarykata.hexagonal.context.domain.CreatedAt
import com.github.caay2000.librarykata.hexagonal.context.domain.FinishedAt
import com.github.caay2000.librarykata.hexagonal.context.domain.Loan
import com.github.caay2000.librarykata.hexagonal.context.domain.LoanId
import java.time.LocalDateTime
import java.util.UUID

object LoanMother {

    fun random(
        id: LoanId = LoanIdMother.random(),
        bookId: BookId = BookId(UUID.randomUUID().toString()),
        userId: AccountId = AccountId(UUID.randomUUID().toString()),
        createdAt: CreatedAt = CreatedAt(LocalDateTime.now()),
        finishedAt: FinishedAt? = null,
    ) = Loan(
        id,
        bookId,
        userId,
        createdAt,
        finishedAt,
    )

    fun finishedLoan(
        finishedAt: FinishedAt = FinishedAt(LocalDateTime.now()),
    ) = random(createdAt = CreatedAt(finishedAt.value.minusDays(3)), finishedAt = finishedAt)
}
