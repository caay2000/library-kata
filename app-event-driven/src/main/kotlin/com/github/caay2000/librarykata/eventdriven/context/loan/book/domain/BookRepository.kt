package com.github.caay2000.librarykata.eventdriven.context.loan.book.domain

interface BookRepository {
    fun save(book: Book): Book

    fun find(bookId: BookId): Book

    fun search(bookIsbn: BookIsbn): List<Book>
}
