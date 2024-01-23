package com.github.caay2000.librarykata.hexagonal.context.domain.book

interface BookRepository {
    fun save(book: Book): Book

    fun find(criteria: FindBookCriteria): Book?

    fun search(criteria: SearchBookCriteria): List<Book>
}

sealed class FindBookCriteria {
    class ById(val id: BookId) : FindBookCriteria()
}

sealed class SearchBookCriteria {
    data object All : SearchBookCriteria()

    data class ByIsbn(val isbn: BookIsbn) : SearchBookCriteria()
}
