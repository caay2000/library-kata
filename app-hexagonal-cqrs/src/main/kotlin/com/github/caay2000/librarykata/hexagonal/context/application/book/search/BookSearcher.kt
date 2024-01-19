package com.github.caay2000.librarykata.hexagonal.context.application.book.search

import com.github.caay2000.librarykata.hexagonal.context.domain.book.Book
import com.github.caay2000.librarykata.hexagonal.context.domain.book.BookRepository
import com.github.caay2000.librarykata.hexagonal.context.domain.book.SearchBookCriteria

class BookSearcher(private val bookRepository: BookRepository) {
    fun invoke(criteria: SearchBookCriteria): List<Book> = bookRepository.search(criteria)
}
