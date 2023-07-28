package com.github.caay2000.librarykata.context.book.domain

import com.github.caay2000.common.ddd.Aggregate
import com.github.caay2000.common.ddd.DomainId
import com.github.caay2000.librarykata.events.book.BookCreatedEvent
import java.util.UUID

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
        fun create(request: CreateBookRequest) = Book(
            id = request.id,
            isbn = request.isbn,
            title = request.title,
            author = request.author,
            pages = request.pages,
            publisher = request.publisher,
            available = BookAvailable(true),
        ).also { book -> book.pushEvent(book.bookCreatedEvent()) }
    }

    private fun bookCreatedEvent() = BookCreatedEvent(
        bookId = id.value,
        isbn = isbn.value,
        title = title.value,
        author = author.value,
        pages = pages.value,
        publisher = publisher.value,
    )

    val isAvailable: Boolean = available.value

    fun updateAvailability(available: BookAvailable): Book = copy(available = available)
}

@JvmInline
value class BookId(val value: UUID) : DomainId {
    override fun toString(): String = value.toString()
}

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
