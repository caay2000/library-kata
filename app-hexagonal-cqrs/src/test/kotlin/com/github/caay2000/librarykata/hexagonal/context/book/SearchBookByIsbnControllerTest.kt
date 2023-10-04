package com.github.caay2000.librarykata.hexagonal.context.book

import com.github.caay2000.common.test.http.assertResponse
import com.github.caay2000.common.test.http.assertStatus
import com.github.caay2000.common.test.mock.MockDateProvider
import com.github.caay2000.common.test.mock.MockIdGenerator
import com.github.caay2000.dikt.DiKt
import com.github.caay2000.librarykata.hexagonal.common.TestUseCases
import com.github.caay2000.librarykata.hexagonal.context.account.mother.AccountMother
import com.github.caay2000.librarykata.hexagonal.context.book.mother.BookIdMother
import com.github.caay2000.librarykata.hexagonal.context.book.mother.BookMother
import com.github.caay2000.librarykata.hexagonal.context.domain.AccountId
import com.github.caay2000.librarykata.hexagonal.context.domain.Book
import com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.serialization.BookViewDocument
import com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.serialization.BookViewResource
import com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.serialization.BookViewResourceAttributes
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.testApplication
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class SearchBookByIsbnControllerTest {

    private val mockIdGenerator = MockIdGenerator()
    private val mockDateProvider = MockDateProvider()
    private val testUseCases = TestUseCases(mockIdGenerator = mockIdGenerator, mockDateProvider = mockDateProvider)

    @BeforeEach
    fun setUp() {
        DiKt.clear()
        DiKt.register(override = true) { mockIdGenerator }
        DiKt.register(override = true) { mockDateProvider }
    }

    @Test
    fun `a book can be retrieved by Isbn`() = testApplication {
        testUseCases.`book is created`(book)
            .assertStatus(HttpStatusCode.Created)

        testUseCases.`find book by isbn`(book.isbn)
            .assertStatus(HttpStatusCode.OK)
            .assertResponse(book.toBookViewDocument())
    }

    @Test
    fun `a book with multiple copies can be retrieved by Isbn`() = testApplication {
        testUseCases.`book is created`(book)
            .assertStatus(HttpStatusCode.Created)
        testUseCases.`book is created`(differentIdBook)
            .assertStatus(HttpStatusCode.Created)

        testUseCases.`find book by isbn`(book.isbn)
            .assertStatus(HttpStatusCode.OK)
            .assertResponse(book.toBookViewDocument(copies = 2, availableCopies = 2))
    }

    @Test
    fun `a book with multiple copies and some of them booked can be retrieved by Isbn and returns de correct amount of copies`() =
        testApplication {
            testUseCases.`account is created`(account)
                .assertStatus(HttpStatusCode.Created)

            testUseCases.`book is created`(book)
                .assertStatus(HttpStatusCode.Created)
            testUseCases.`book is created`(differentIdBook)
                .assertStatus(HttpStatusCode.Created)

            testUseCases.`loan is created`(bookIsbn = book.isbn, accountId = AccountId(account.id.value))
                .assertStatus(HttpStatusCode.Created)

            testUseCases.`find book by isbn`(book.isbn)
                .assertStatus(HttpStatusCode.OK)
                .assertResponse(book.toBookViewDocument(copies = 2, availableCopies = 1))
        }

    private val book = BookMother.random()
    private val differentIdBook = book.copy(id = BookIdMother.random())

    private val account = AccountMother.random()

    private fun Book.toBookViewDocument(
        copies: Int = 1,
        availableCopies: Int = if (isAvailable) 1 else 0,
    ) = BookViewDocument(
        data = BookViewResource(
            attributes = BookViewResourceAttributes(
                isbn = isbn.value,
                title = title.value,
                author = author.value,
                pages = pages.value,
                publisher = publisher.value,
                copies = copies,
                availableCopies = availableCopies,
            ),
        ),
    )
}
