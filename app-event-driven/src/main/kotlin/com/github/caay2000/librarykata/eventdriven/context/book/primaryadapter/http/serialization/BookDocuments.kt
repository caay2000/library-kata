package com.github.caay2000.librarykata.eventdriven.context.book.primaryadapter.http.serialization

import com.github.caay2000.common.serialization.UUIDSerializer
import com.github.caay2000.librarykata.eventdriven.context.book.domain.Book
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class AllBooksDocument(val books: List<BookDocument>) {
    constructor(vararg books: BookDocument) : this(books.toList())
}

@Serializable
data class BookByIdDocument(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val isbn: String,
    val title: String,
    val author: String,
    val pages: Int,
    val publisher: String,
    val available: Boolean,
)

@Serializable
data class BookDocument(
    val isbn: String,
    val title: String,
    val author: String,
    val pages: Int,
    val publisher: String,
    val copies: Int,
    val availableCopies: Int,
)

fun List<Book>.toAllBooksDocument() =
    this.groupBy { it.isbn }
        .toSortedMap(compareBy { it.value })
        .map { (key, books) ->
            val sample = books.first()
            BookDocument(
                isbn = key.value,
                title = sample.title.value,
                author = sample.author.value,
                pages = sample.pages.value,
                publisher = sample.publisher.value,
                copies = books.count(),
                availableCopies = books.count { book -> book.available.value },
            )
        }
        .let { AllBooksDocument(books = it) }

fun Book.toBookDocument() =
    BookDocument(
        isbn = isbn.value,
        title = title.value,
        author = author.value,
        pages = pages.value,
        publisher = publisher.value,
        copies = 1,
        availableCopies = if (available.value) 1 else 0,
    )

fun List<Book>.toBookDocument() = toAllBooksDocument().books.first()

fun Book.toBookByIdDocument() =
    BookByIdDocument(
        id = id.value,
        isbn = isbn.value,
        title = title.value,
        author = author.value,
        pages = pages.value,
        publisher = publisher.value,
        available = available.value,
    )

@Serializable
data class BookCreateRequestDocument(
    val isbn: String,
    val title: String,
    val author: String,
    val pages: Int,
    val publisher: String,
)
