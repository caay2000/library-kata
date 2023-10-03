package com.github.caay2000.librarykata.eventdriven.context.book.mother

import com.github.caay2000.librarykata.eventdriven.context.account.domain.LoanId
import com.github.caay2000.librarykata.eventdriven.context.account.primaryadapter.http.serialization.LoanDocument
import com.github.caay2000.librarykata.eventdriven.context.book.domain.Book
import java.time.LocalDateTime

object LoanDocumentMother {

    fun Book.toLoanDocument(loanId: LoanId, startedAt: LocalDateTime, finishedAt: LocalDateTime? = null) =
        LoanDocument(
            id = loanId.value,
            bookId = id.value,
            isbn = isbn.value,
            title = title.value,
            author = author.value,
            pages = pages.value,
            publisher = publisher.value,
            startLoan = startedAt,
            finishLoan = finishedAt,
        )
}
