package com.github.caay2000.librarykata.core.context.book

import com.github.caay2000.common.jsonapi.jsonApiErrorDocument
import com.github.caay2000.common.test.http.assertJsonApiErrorDocument
import com.github.caay2000.common.test.http.assertJsonApiResponse
import com.github.caay2000.common.test.http.assertStatus
import com.github.caay2000.common.test.mock.MockDateProvider
import com.github.caay2000.common.test.mock.MockIdGenerator
import com.github.caay2000.dikt.DiKt
import com.github.caay2000.librarykata.core.common.TestUseCases
import com.github.caay2000.librarykata.core.context.account.mother.AccountMother
import com.github.caay2000.librarykata.core.context.book.mother.BookDocumentMother
import com.github.caay2000.librarykata.core.context.book.mother.BookMother
import com.github.caay2000.librarykata.core.context.loan.mother.LoanMother
import com.github.caay2000.librarykata.hexagonal.context.account.domain.CurrentLoans
import com.github.caay2000.librarykata.hexagonal.context.account.domain.TotalLoans
import com.github.caay2000.librarykata.hexagonal.context.book.domain.BookAvailable
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.testApplication
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class FindBookControllerTest {
    private val mockIdGenerator = MockIdGenerator()
    private val mockDateProvider = MockDateProvider()
    private val testUseCases = TestUseCases(mockIdGenerator = mockIdGenerator, mockDateProvider = mockDateProvider)

    // Account and Loan Includes needed

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
            testUseCases.`account is created with a loan`(account, book, loan)

            val expected = BookDocumentMother.random(notAvailableBook, listOf(account), listOf(loan))
            testUseCases.`find book by id`(book.id)
                .assertStatus(HttpStatusCode.OK)
                .assertJsonApiResponse(expected)
        }

    @Test
    fun `a lent book can be retrieved by Id including loan information`() =
        testApplication {
            testUseCases.`account is created with a loan`(account, book, loan)

            val expected = BookDocumentMother.random(notAvailableBook, listOf(account), listOf(loan), listOf("loan"))
            testUseCases.`find book by id`(book.id, listOf("loan"))
                .assertStatus(HttpStatusCode.OK)
                .assertJsonApiResponse(expected)
        }

    @Test
    fun `a lent book can be retrieved by Id including loan and account information`() =
        testApplication {
            testUseCases.`account is created with a loan`(account, book, loan)

            val expectedAccount = account.copy(currentLoans = CurrentLoans(1), totalLoans = TotalLoans(1))
            val expected = BookDocumentMother.random(notAvailableBook, listOf(expectedAccount), listOf(loan), listOf("account", "loan"))
            testUseCases.`find book by id`(book.id, listOf("account", "loan"))
                .assertStatus(HttpStatusCode.OK)
                .assertJsonApiResponse(expected)
        }

    @Test
    fun `fails when finding a non existing BookId`() =
        testApplication {
            testUseCases.`find book by id`(book.id)
                .assertStatus(HttpStatusCode.NotFound)
                .assertJsonApiErrorDocument(
                    jsonApiErrorDocument(
                        status = HttpStatusCode.NotFound,
                        title = "BookNotFoundError",
                        detail = "Book ${book.id.value} not found",
                    ),
                )
        }

    private val account = AccountMother.random()
    private val book = BookMother.random()
    private val loan = LoanMother.random(bookId = book.id, accountId = account.id)
    private val notAvailableBook = book.copy(available = BookAvailable.notAvailable())
}
