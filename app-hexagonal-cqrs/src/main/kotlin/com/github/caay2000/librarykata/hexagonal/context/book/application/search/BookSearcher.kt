package com.github.caay2000.librarykata.hexagonal.context.book.application.search

import com.github.caay2000.librarykata.hexagonal.context.book.domain.Book
import com.github.caay2000.librarykata.hexagonal.context.book.domain.BookRepository
import com.github.caay2000.librarykata.hexagonal.context.book.domain.SearchBookCriteria

class BookSearcher(private val bookRepository: BookRepository) {
    fun invoke(criteria: SearchBookCriteria): List<Book> = bookRepository.search(criteria)
}
