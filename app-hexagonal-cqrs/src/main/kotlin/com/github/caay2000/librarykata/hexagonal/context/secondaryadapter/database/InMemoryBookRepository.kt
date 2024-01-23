package com.github.caay2000.librarykata.hexagonal.context.secondaryadapter.database

import com.github.caay2000.librarykata.hexagonal.context.domain.book.Book
import com.github.caay2000.librarykata.hexagonal.context.domain.book.BookRepository
import com.github.caay2000.librarykata.hexagonal.context.domain.book.FindBookCriteria
import com.github.caay2000.librarykata.hexagonal.context.domain.book.SearchBookCriteria
import com.github.caay2000.memorydb.InMemoryDatasource

class InMemoryBookRepository(private val datasource: InMemoryDatasource) : BookRepository {
    override fun save(book: Book): Book = datasource.save(TABLE_NAME, book.id.value, book)

    override fun find(criteria: FindBookCriteria): Book? =
        when (criteria) {
            is FindBookCriteria.ById -> datasource.getById<Book>(TABLE_NAME, criteria.id.value)
        }

    override fun search(criteria: SearchBookCriteria): List<Book> =
        when (criteria) {
            SearchBookCriteria.All -> datasource.getAll(TABLE_NAME)
            is SearchBookCriteria.ByIsbn -> datasource.getAll<Book>(TABLE_NAME).filter { it.isbn == criteria.isbn }
        }

    companion object {
        private const val TABLE_NAME = "book"
    }
}
