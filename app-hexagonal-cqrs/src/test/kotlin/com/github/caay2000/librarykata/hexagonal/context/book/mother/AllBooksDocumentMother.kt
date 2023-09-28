package com.github.caay2000.librarykata.hexagonal.context.book.mother

import com.github.caay2000.librarykata.hexagonal.context.domain.Book
import com.github.caay2000.librarykata.hexagonal.context.domain.BookAvailable
import com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.serialization.AllBooksDocument
import com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.serialization.toAllBooksDocument

object AllBooksDocumentMother {

    fun from(vararg books: BookCopies): AllBooksDocument =
        books.toList()
            .flatMap { (book, copies, available) ->
                val list: MutableList<Book> = mutableListOf()
                repeat(copies) { index ->
                    if (index < available) {
                        list.add(book)
                    } else {
                        list.add(book.copy(available = BookAvailable.notAvailable()))
                    }
                }
                list
            }.toAllBooksDocument()
}

data class BookCopies(
    val book: Book,
    val copies: Int = 1,
    val available: Int = copies,
)
