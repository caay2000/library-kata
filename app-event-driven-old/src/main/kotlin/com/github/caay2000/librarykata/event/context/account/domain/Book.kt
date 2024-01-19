package com.github.caay2000.librarykata.event.context.account.domain

import java.util.UUID

data class Book(
    val id: BookId,
    val isbn: BookIsbn,
    val title: BookTitle,
    val author: BookAuthor,
    val pages: BookPages,
    val publisher: BookPublisher,
)

@JvmInline
value class BookId(val value: UUID) {
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
