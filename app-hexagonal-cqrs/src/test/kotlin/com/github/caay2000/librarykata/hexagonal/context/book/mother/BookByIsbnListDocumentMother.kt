package com.github.caay2000.librarykata.hexagonal.context.book.mother

import com.github.caay2000.common.jsonapi.JsonApiListDocument
import com.github.caay2000.common.jsonapi.context.book.BookByIsbnResource
import com.github.caay2000.librarykata.hexagonal.configuration.jsonMapper
import com.github.caay2000.librarykata.hexagonal.context.domain.book.Book
import com.github.caay2000.librarykata.hexagonal.context.domain.book.BookAvailable
import com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.serialization.toJsonApiListDocument
import kotlinx.serialization.encodeToString

object JsonApiListDocumentMother {

    fun from(vararg books: BookCopies): JsonApiListDocument<BookByIsbnResource> =
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
            }
            .toJsonApiListDocument()

    fun json(vararg books: BookCopies): String = jsonMapper.encodeToString(from(*books))
}

data class BookCopies(
    val book: Book,
    val copies: Int = 1,
    val available: Int = copies,
)
