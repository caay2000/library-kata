package com.github.caay2000.librarykata.core.context.book.mother

import com.github.caay2000.librarykata.hexagonal.context.book.domain.Book
import com.github.caay2000.librarykata.hexagonal.context.book.primaryadapter.http.transformer.toJsonApiBookDocument

object BookDocumentMother {
    internal fun random(book: Book = BookMother.random()) = book.toJsonApiBookDocument()
}
