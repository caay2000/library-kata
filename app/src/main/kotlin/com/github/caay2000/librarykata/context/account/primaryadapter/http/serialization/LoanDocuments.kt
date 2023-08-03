package com.github.caay2000.librarykata.context.account.primaryadapter.http.serialization

import com.github.caay2000.common.serialization.LocalDateTimeSerializer
import com.github.caay2000.common.serialization.UUIDSerializer
import com.github.caay2000.librarykata.context.account.domain.Book
import com.github.caay2000.librarykata.context.account.domain.Loan
import kotlinx.serialization.Serializable
import java.time.LocalDateTime
import java.util.UUID

@Serializable
data class LoanByUserIdDocument(
    @Serializable(with = UUIDSerializer::class)
    val accountId: UUID,
    val loans: List<LoanDocument>,
)

@Serializable
data class LoanDocument(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    @Serializable(with = UUIDSerializer::class)
    val bookId: UUID,
    val isbn: String,
    val title: String,
    val author: String,
    val pages: Int,
    val publisher: String,
    @Serializable(with = LocalDateTimeSerializer::class)
    val startLoan: LocalDateTime,
    @Serializable(with = LocalDateTimeSerializer::class)
    val finishLoan: LocalDateTime?,
)

fun Loan.toLoanDocument(book: Book) =
    LoanDocument(
        id = id.value,
        bookId = book.id.value,
        isbn = book.isbn.value,
        title = book.title.value,
        author = book.author.value,
        pages = book.pages.value,
        publisher = book.publisher.value,
        startLoan = startedAt.value,
        finishLoan = finishedAt?.value,
    )
