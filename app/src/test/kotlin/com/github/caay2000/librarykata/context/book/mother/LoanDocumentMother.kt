package com.github.caay2000.librarykata.context.book.mother

import com.github.caay2000.librarykata.context.book.domain.Book
import com.github.caay2000.librarykata.context.book.domain.LoanId
import com.github.caay2000.librarykata.context.book.primaryadapter.http.serialization.LoanDocument
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
