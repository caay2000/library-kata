package com.github.caay2000.librarykata.hexagonal.context.book

import com.github.caay2000.common.test.http.assertJsonResponse
import com.github.caay2000.common.test.http.assertStatus
import com.github.caay2000.common.test.mock.MockDateProvider
import com.github.caay2000.common.test.mock.MockIdGenerator
import com.github.caay2000.dikt.DiKt
import com.github.caay2000.librarykata.hexagonal.common.TestUseCases
import com.github.caay2000.librarykata.hexagonal.context.account.mother.AccountMother
import com.github.caay2000.librarykata.hexagonal.context.book.mother.BookByIsbnListDocumentMother
import com.github.caay2000.librarykata.hexagonal.context.book.mother.BookCopies
import com.github.caay2000.librarykata.hexagonal.context.book.mother.BookMother
import com.github.caay2000.librarykata.hexagonal.context.domain.AccountId
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
    fun `all books can be searched`() = testApplication {
        testUseCases.`multiple copies of the same book are created`(book, 5)
        testUseCases.`multiple copies of the same book are created`(differentBook, 3)
        testUseCases.`book is created`(anotherBook)
        testUseCases.`account is created`(account)
        testUseCases.`loan is created`(bookIsbn = book.isbn, accountId = AccountId(account.id.value))

        testUseCases.`search all books`()
            .assertStatus(HttpStatusCode.OK)
            .assertJsonResponse(
                BookByIsbnListDocumentMother.from(
                    BookCopies(book, 5, 4),
                    BookCopies(differentBook, 3),
                    BookCopies(anotherBook),
                ),
            )
    }

    private val book = BookMother.random()
    private val differentBook = BookMother.random()
    private val anotherBook = BookMother.random()

    private val account = AccountMother.random()
}
