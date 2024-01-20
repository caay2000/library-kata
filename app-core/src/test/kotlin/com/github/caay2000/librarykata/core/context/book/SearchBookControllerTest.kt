package com.github.caay2000.librarykata.core.context.book

import com.github.caay2000.common.test.http.assertJsonApiResponse
import com.github.caay2000.common.test.http.assertStatus
import com.github.caay2000.common.test.mock.MockDateProvider
import com.github.caay2000.common.test.mock.MockIdGenerator
import com.github.caay2000.dikt.DiKt
import com.github.caay2000.librarykata.core.common.TestUseCases
import com.github.caay2000.librarykata.core.context.account.mother.AccountMother
import com.github.caay2000.librarykata.core.context.book.mother.BookGroupDocumentMother
import com.github.caay2000.librarykata.core.context.book.mother.BookMother
import com.github.caay2000.librarykata.core.context.loan.mother.LoanMother
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.testApplication
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class SearchBookControllerTest {
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
    fun `a book can be searched by Isbn`() =
        testApplication {
            testUseCases.`book is created`(book)

            val expected = BookGroupDocumentMother.random(book)
            testUseCases.`find book by isbn`(book.isbn)
                .assertStatus(HttpStatusCode.OK)
                .assertJsonApiResponse(expected)
        }

    @Test
    fun `no book is returned if isbn is not found`() =
        testApplication {
            val expected = BookGroupDocumentMother.empty()
            testUseCases.`find book by isbn`(book.isbn)
                .assertStatus(HttpStatusCode.OK)
                .assertJsonApiResponse(expected)
        }

    @Test
    fun `a book with multiple copies can be retrieved by Isbn`() =
        testApplication {
            testUseCases.`book is created`(book)
            testUseCases.`book is created`(sameBook)
            testUseCases.`book is created`(differentBook)

            val expected = BookGroupDocumentMother.random(book, copies = 2, available = 2)
            testUseCases.`find book by isbn`(book.isbn)
                .assertStatus(HttpStatusCode.OK)
                .assertJsonApiResponse(expected)
        }

    @Test
    fun `a book with multiple copies and some of them booked can be retrieved by Isbn and returns de correct amount of copies`() =
        testApplication {
            testUseCases.`account is created with a loan`(account, book, loan)
            testUseCases.`book is created`(sameBook)
            testUseCases.`book is created`(differentBook)

            val expected = BookGroupDocumentMother.random(book, copies = 2, available = 1, listOf(loan))
            testUseCases.`find book by isbn`(book.isbn)
                .assertStatus(HttpStatusCode.OK)
                .assertJsonApiResponse(expected)
        }

    @Test
    fun `all books can be searched`() =
        testApplication {
            testUseCases.`account is created`(account)
            testUseCases.`multiple copies of the same book are created`(book, 5)
            testUseCases.`multiple copies of the same book are created`(differentBook, 3)
            testUseCases.`loan is created`(loan)

            val expected =
                BookGroupDocumentMother.random(
                    listOf(
                        BookGroupDocumentMother.BookCopies(book, 5, 4),
                        BookGroupDocumentMother.BookCopies(differentBook, 3),
                    ),
                    listOf(loan),
                )

            testUseCases.`search book`()
                .assertStatus(HttpStatusCode.OK)
                .assertJsonApiResponse(expected)
        }

    private val book = BookMother.random()
    private val sameBook = BookMother.random(isbn = book.isbn.value)
    private val differentBook = BookMother.random()

    private val account = AccountMother.random()
    private val loan = LoanMother.random(bookId = book.id, accountId = account.id)
}
