package com.github.caay2000.librarykata.hexagonal.context.domain.book

data class Book(
    val id: BookId,
    val isbn: BookIsbn,
    val title: BookTitle,
    val author: BookAuthor,
    val pages: BookPages,
    val publisher: BookPublisher,
    val available: BookAvailable,
) {
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
            )
    }

    fun available(): Book = updateAvailability(BookAvailable.available())

    fun unavailable(): Book = updateAvailability(BookAvailable.notAvailable())

    private fun updateAvailability(available: BookAvailable): Book = copy(available = available)

    val isAvailable: Boolean = available.value
}

@JvmInline
value class BookId(val value: String)

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
