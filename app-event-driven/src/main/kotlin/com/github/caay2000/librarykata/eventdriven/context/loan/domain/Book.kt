package com.github.caay2000.librarykata.eventdriven.context.loan.domain

data class Book(
    val id: BookId,
    val isbn: BookIsbn,
    val available: BookAvailable,
) {

    companion object {
        fun create(id: BookId, isbn: BookIsbn) = Book(
            id = id,
            isbn = isbn,
            available = BookAvailable(true),
        )
    }

    val isAvailable: Boolean = available.value

    fun updateAvailability(available: BookAvailable): Book = copy(available = available)
}

@JvmInline
value class BookIsbn(val value: String)

@JvmInline
value class BookAvailable(val value: Boolean)
