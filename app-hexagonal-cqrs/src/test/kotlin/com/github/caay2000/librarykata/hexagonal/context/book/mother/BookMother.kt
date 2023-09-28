package com.github.caay2000.librarykata.hexagonal.context.book.mother

import com.github.caay2000.common.test.RandomStringGenerator
import com.github.caay2000.librarykata.hexagonal.context.domain.Book
import com.github.caay2000.librarykata.hexagonal.context.domain.BookAuthor
import com.github.caay2000.librarykata.hexagonal.context.domain.BookAvailable
import com.github.caay2000.librarykata.hexagonal.context.domain.BookId
import com.github.caay2000.librarykata.hexagonal.context.domain.BookIsbn
import com.github.caay2000.librarykata.hexagonal.context.domain.BookPages
import com.github.caay2000.librarykata.hexagonal.context.domain.BookPublisher
import com.github.caay2000.librarykata.hexagonal.context.domain.BookTitle
import kotlin.random.Random

object BookMother {

    fun random(
        id: BookId = BookIdMother.random(),
        isbn: String = BookIsbnMother.random().value,
        title: String = RandomStringGenerator.randomString(),
        author: String = RandomStringGenerator.randomString(),
        pages: Int = Random.nextInt(20, 1000),
        publisher: String = RandomStringGenerator.randomString(),
        available: Boolean = true,
    ) = Book(
        id = id,
        isbn = BookIsbn(isbn),
        title = BookTitle(title),
        author = BookAuthor(author),
        pages = BookPages(pages),
        publisher = BookPublisher(publisher),
        available = BookAvailable(available),
    )
}
