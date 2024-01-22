package com.github.caay2000.librarykata.eventdriven.context.loan.application.book.availability

import com.github.caay2000.librarykata.eventdriven.context.loan.domain.Book
import com.github.caay2000.librarykata.eventdriven.context.loan.domain.BookAvailable
import com.github.caay2000.librarykata.eventdriven.context.loan.domain.BookId
import com.github.caay2000.librarykata.eventdriven.context.loan.domain.BookRepository

class AvailabilityUpdater(private val bookRepository: BookRepository) {
    fun invoke(
        bookId: BookId,
        available: BookAvailable,
    ) = bookRepository.find(bookId)
        .updateAvailability(available)
        .save()

    private fun Book.save() = bookRepository.save(this)
}
