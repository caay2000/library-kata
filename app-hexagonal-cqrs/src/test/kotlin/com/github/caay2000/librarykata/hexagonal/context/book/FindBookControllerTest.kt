package com.github.caay2000.librarykata.hexagonal.context.book

import com.github.caay2000.common.test.http.assertJsonApiResponse
import com.github.caay2000.common.test.http.assertStatus
import com.github.caay2000.common.test.mock.MockDateProvider
import com.github.caay2000.common.test.mock.MockIdGenerator
import com.github.caay2000.dikt.DiKt
import com.github.caay2000.librarykata.hexagonal.common.TestUseCases
import com.github.caay2000.librarykata.hexagonal.context.account.mother.AccountMother
import com.github.caay2000.librarykata.hexagonal.context.book.mother.BookDocumentMother
import com.github.caay2000.librarykata.hexagonal.context.book.mother.BookMother
import com.github.caay2000.librarykata.hexagonal.context.domain.account.AccountId
import com.github.caay2000.librarykata.hexagonal.context.domain.book.BookAvailable
import com.github.caay2000.librarykata.hexagonal.context.loan.mother.LoanMother
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.testApplication
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class FindBookControllerTest {
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
    fun `a book can be retrieved by Id`() =
        testApplication {
            testUseCases.`book is created`(book)

            val expected = BookDocumentMother.random(book)
            testUseCases.`find book by id`(book.id)
                .assertStatus(HttpStatusCode.OK)
                .assertJsonApiResponse(expected)
        }

    @Test
    fun `a lent book can be retrieved by Id`() =
        testApplication {
            testUseCases.`book is created`(book)
            testUseCases.`account is created`(account)
            testUseCases.`loan is created`(
                id = loan.id,
                bookIsbn = book.isbn,
                accountId = AccountId(account.id.value),
            )

            val expected = BookDocumentMother.random(notAvailableBook, listOf(loan))
            testUseCases.`find book by id`(book.id)
                .assertStatus(HttpStatusCode.OK)
                .assertJsonApiResponse(expected)
        }

    @Test
    fun `a lent book can be retrieved by Id including loan information`() =
        testApplication {
            testUseCases.`book is created`(book)
            testUseCases.`account is created`(account)
            testUseCases.`loan is created`(
                id = loan.id,
                bookIsbn = book.isbn,
                accountId = AccountId(account.id.value),
                createdAt = loan.createdAt,
            )

            val expected = BookDocumentMother.random(notAvailableBook, listOf(loan), listOf("loan"))
            testUseCases.`find book by id`(book.id, listOf("loan"))
                .assertStatus(HttpStatusCode.OK)
                .assertJsonApiResponse(expected)
        }

    private val account = AccountMother.random()
    private val book = BookMother.random()
    private val loan = LoanMother.random(bookId = book.id, accountId = account.id)
    private val notAvailableBook = book.copy(available = BookAvailable.notAvailable())
}
