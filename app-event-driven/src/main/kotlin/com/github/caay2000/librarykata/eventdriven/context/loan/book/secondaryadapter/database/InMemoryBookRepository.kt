package com.github.caay2000.librarykata.eventdriven.context.loan.book.secondaryadapter.database

import com.github.caay2000.librarykata.eventdriven.context.loan.book.domain.Book
import com.github.caay2000.librarykata.eventdriven.context.loan.book.domain.BookId
import com.github.caay2000.librarykata.eventdriven.context.loan.book.domain.BookIsbn
import com.github.caay2000.librarykata.eventdriven.context.loan.book.domain.BookRepository
import com.github.caay2000.memorydb.InMemoryDatasource

class InMemoryBookRepository(private val datasource: InMemoryDatasource) : BookRepository {
    override fun save(book: Book): Book = datasource.save(TABLE_NAME, book.id.value, book)

    override fun find(bookId: BookId): Book = datasource.getById<Book>(TABLE_NAME, bookId.value)!!

    override fun search(bookIsbn: BookIsbn): List<Book> = datasource.getAll<Book>(TABLE_NAME).filter { it.isbn == bookIsbn }

    companion object {
        private const val TABLE_NAME = "loan.book"
    }
}
