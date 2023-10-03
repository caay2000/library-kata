package com.github.caay2000.librarykata.hexagonal.context.book.mother

import com.github.caay2000.librarykata.hexagonal.context.domain.Book
import com.github.caay2000.librarykata.hexagonal.context.domain.LoanId
import com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.serialization.LoanDocument
import java.time.LocalDateTime
import java.util.UUID

object LoanDocumentMother {

    fun Book.toLoanDocument(loanId: LoanId, startedAt: LocalDateTime, finishedAt: LocalDateTime? = null) =
        LoanDocument(
            id = UUID.fromString(loanId.value),
            bookId = UUID.fromString(id.value),
            isbn = isbn.value,
            title = title.value,
            author = author.value,
            pages = pages.value,
            publisher = publisher.value,
            startLoan = startedAt,
            finishLoan = finishedAt,
        )
}
