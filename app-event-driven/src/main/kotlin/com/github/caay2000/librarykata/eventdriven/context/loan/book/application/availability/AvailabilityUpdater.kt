package com.github.caay2000.librarykata.eventdriven.context.loan.book.application.availability

import com.github.caay2000.librarykata.eventdriven.context.loan.book.domain.Book
import com.github.caay2000.librarykata.eventdriven.context.loan.book.domain.BookAvailable
import com.github.caay2000.librarykata.eventdriven.context.loan.book.domain.BookId
import com.github.caay2000.librarykata.eventdriven.context.loan.book.domain.BookRepository

class AvailabilityUpdater(private val bookRepository: BookRepository) {
    fun invoke(
        bookId: BookId,
        available: BookAvailable,
    ) = bookRepository.find(bookId)
        .updateAvailability(available)
        .save()

    private fun Book.save() = bookRepository.save(this)
}
