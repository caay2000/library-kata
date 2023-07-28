package com.github.caay2000.projectskeleton.context.loan.mother

import com.github.caay2000.projectskeleton.context.loan.domain.BookId
import com.github.caay2000.projectskeleton.context.loan.domain.CreatedAt
import com.github.caay2000.projectskeleton.context.loan.domain.FinishedAt
import com.github.caay2000.projectskeleton.context.loan.domain.Loan
import com.github.caay2000.projectskeleton.context.loan.domain.LoanId
import com.github.caay2000.projectskeleton.context.loan.domain.UserId
import java.time.LocalDateTime
import java.util.UUID

object LoanMother {

    fun random(
        id: LoanId = LoanIdMother.random(),
        bookId: BookId = BookId(UUID.randomUUID()),
        userId: UserId = UserId(UUID.randomUUID()),
        createdAt: CreatedAt = CreatedAt(LocalDateTime.now()),
        finishedAt: FinishedAt? = null,
    ) = Loan(
        id,
        bookId,
        userId,
        createdAt,
        finishedAt,
    )

    fun random(
        id: UUID = UUID.randomUUID(),
        bookId: UUID = UUID.randomUUID(),
        userId: UUID = UUID.randomUUID(),
    ) = random(
        id = LoanId(id),
        bookId = BookId(bookId),
        userId = UserId(userId),
    )
}
