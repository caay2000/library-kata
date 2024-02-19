package com.github.caay2000.librarykata.eventdriven.context.book.application.update

import com.github.caay2000.librarykata.eventdriven.context.book.domain.Book
import com.github.caay2000.librarykata.eventdriven.context.book.domain.BookAvailable
import com.github.caay2000.librarykata.eventdriven.context.book.domain.BookId
import com.github.caay2000.librarykata.eventdriven.context.book.domain.BookRepository
import com.github.caay2000.librarykata.eventdriven.context.book.domain.FindBookCriteria

class AvailabilityUpdater(private val bookRepository: BookRepository) {
    fun invoke(
        bookId: BookId,
        available: BookAvailable,
    ) = bookRepository.find(FindBookCriteria.ById(bookId))!!
        .updateAvailability(available)
        .save()

    private fun Book.save() = bookRepository.save(this)
}
