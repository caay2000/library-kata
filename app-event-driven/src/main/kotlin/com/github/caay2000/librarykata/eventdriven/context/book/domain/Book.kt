package com.github.caay2000.librarykata.eventdriven.context.book.domain

import com.github.caay2000.common.ddd.Aggregate
import com.github.caay2000.common.ddd.AggregateId
import com.github.caay2000.librarykata.eventdriven.events.book.BookCreatedEvent

data class Book(
    override val id: BookId,
    val isbn: BookIsbn,
    val title: BookTitle,
    val author: BookAuthor,
    val pages: BookPages,
    val publisher: BookPublisher,
    val available: BookAvailable,
) : Aggregate() {
    companion object {
        fun create(request: CreateBookRequest) =
            Book(
                id = request.id,
                isbn = request.isbn,
                title = request.title,
                author = request.author,
                pages = request.pages,
                publisher = request.publisher,
                available = BookAvailable(true),
            ).also { book -> book.pushEvent(book.bookCreatedEvent()) }
    }

    private fun bookCreatedEvent() =
        BookCreatedEvent(
            bookId = id.value,
            isbn = isbn.value,
            title = title.value,
            author = author.value,
            pages = pages.value,
            publisher = publisher.value,
        )

    fun available(): Book = updateAvailability(BookAvailable.available())

    fun unavailable(): Book = updateAvailability(BookAvailable.notAvailable())

    private fun updateAvailability(available: BookAvailable): Book = copy(available = available)

    val isAvailable: Boolean = available.value
}

@JvmInline
value class BookId(val value: String) : AggregateId

@JvmInline
value class BookIsbn(val value: String)

@JvmInline
value class BookTitle(val value: String)

@JvmInline
value class BookAuthor(val value: String)

@JvmInline
value class BookPages(val value: Int)

@JvmInline
value class BookPublisher(val value: String)

@JvmInline
value class BookAvailable(val value: Boolean) {
    companion object {
        fun available() = BookAvailable(true)

        fun notAvailable() = BookAvailable(false)
    }
}

data class CreateBookRequest(
    val id: BookId,
    val isbn: BookIsbn,
    val title: BookTitle,
    val author: BookAuthor,
    val pages: BookPages,
    val publisher: BookPublisher,
)
