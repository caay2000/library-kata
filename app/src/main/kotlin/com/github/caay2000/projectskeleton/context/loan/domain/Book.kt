package com.github.caay2000.projectskeleton.context.loan.domain

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

    fun updateAvailability(available: Boolean): Book = copy(available = BookAvailable(available))
}

@JvmInline
value class BookIsbn(val value: String) {
    override fun toString(): String = value
}

@JvmInline
value class BookAvailable(val value: Boolean)
