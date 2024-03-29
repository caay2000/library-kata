package com.github.caay2000.librarykata.core.context.account

import com.github.caay2000.common.jsonapi.jsonApiErrorDocument
import com.github.caay2000.common.test.http.assertJsonApiErrorDocument
import com.github.caay2000.common.test.http.assertJsonApiResponse
import com.github.caay2000.common.test.http.assertStatus
import com.github.caay2000.common.test.mock.MockDateProvider
import com.github.caay2000.common.test.mock.MockIdGenerator
import com.github.caay2000.dikt.DiKt
import com.github.caay2000.librarykata.core.common.TestUseCases
import com.github.caay2000.librarykata.core.context.account.mother.AccountDocumentMother
import com.github.caay2000.librarykata.core.context.account.mother.AccountMother
import com.github.caay2000.librarykata.core.context.book.mother.BookMother
import com.github.caay2000.librarykata.core.context.loan.mother.LoanMother
import com.github.caay2000.librarykata.hexagonal.context.account.domain.AccountId
import com.github.caay2000.librarykata.hexagonal.context.account.domain.CurrentLoans
import com.github.caay2000.librarykata.hexagonal.context.account.domain.TotalLoans
import com.github.caay2000.librarykata.hexagonal.context.book.domain.BookId
import com.github.caay2000.librarykata.hexagonal.context.loan.domain.FinishedAt
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.testApplication
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class FindAccountControllerTest {
    private val mockIdGenerator = MockIdGenerator()
    private val mockDateProvider = MockDateProvider()
    private val testUseCases =
        TestUseCases(
            mockIdGenerator = mockIdGenerator,
            mockDateProvider = mockDateProvider,
        )

    // Book and Loan Includes needed

    @BeforeEach
    fun setUp() {
        DiKt.clear()
        DiKt.register(override = true) { mockIdGenerator }
        DiKt.register(override = true) { mockDateProvider }
    }

    @Test
    fun `an account can be retrieved`() =
        testApplication {
            testUseCases.`account is created`(account)

            val expected = AccountDocumentMother.random(account)
            testUseCases.`find account`(account.id)
                .assertStatus(HttpStatusCode.OK)
                .assertJsonApiResponse(expected)
        }

    @Test
    fun `a user without loans has no loans`() =
        testApplication {
            testUseCases.`account is created`(account)

            val expected = AccountDocumentMother.random(account, emptyList())
            testUseCases.`find account`(account.id, listOf("loan"))
                .assertStatus(HttpStatusCode.OK)
                .assertJsonApiResponse(expected)
        }

    @Test
    fun `a user with one loan retrieves it`() =
        testApplication {
            testUseCases.`account is created with a loan`(account, book, loan)

            val expectedAccount = account.copy(currentLoans = CurrentLoans(1), totalLoans = TotalLoans(1))
            val expected = AccountDocumentMother.random(expectedAccount, listOf(loan))
            testUseCases.`find account`(account.id)
                .assertStatus(HttpStatusCode.OK)
                .assertJsonApiResponse(expected)
        }

    @Test
    fun `a user with one loan retrieves it including loan information`() =
        testApplication {
            testUseCases.`account is created with a loan`(account, book, loan)

            val expectedAccount = account.copy(currentLoans = CurrentLoans(1), totalLoans = TotalLoans(1))
            val expected = AccountDocumentMother.random(expectedAccount, listOf(loan), listOf("loan"))
            testUseCases.`find account`(account.id, listOf("loan"))
                .assertStatus(HttpStatusCode.OK)
                .assertJsonApiResponse(expected)
        }

    @Test
    fun `retrieve a user with multiple loans including loan data`() =
        testApplication {
            testUseCases.`account is created with a loan`(account, book, loan)

            testUseCases.`book is created`(anotherBook)
            testUseCases.`loan is created`(
                id = anotherLoan.id,
                bookIsbn = anotherBook.isbn,
                accountId = AccountId(account.id.value),
                createdAt = anotherLoan.createdAt,
            )
            testUseCases.`loan is finished`(bookId = BookId(anotherBook.id.value), finishedAt = anotherLoan.finishedAt)

            val expectedAccount = account.copy(currentLoans = CurrentLoans(1), totalLoans = TotalLoans(2))
            val expected = AccountDocumentMother.random(expectedAccount, listOf(loan), listOf("loan"))
            testUseCases.`find account`(account.id, listOf("loan"))
                .assertStatus(HttpStatusCode.OK)
                .assertJsonApiResponse(expected)
        }

    @Test
    fun `retrieve a user with multiple loans including loan and account data`() =
        testApplication {
            testUseCases.`account is created with a loan`(account, book, loan)

            testUseCases.`book is created`(anotherBook)
            testUseCases.`loan is created`(
                id = anotherLoan.id,
                bookIsbn = anotherBook.isbn,
                accountId = AccountId(account.id.value),
                createdAt = anotherLoan.createdAt,
            )
            testUseCases.`loan is finished`(bookId = BookId(anotherBook.id.value), finishedAt = anotherLoan.finishedAt)

            val expectedAccount = account.copy(currentLoans = CurrentLoans(1), totalLoans = TotalLoans(2))
            val expected = AccountDocumentMother.random(expectedAccount, listOf(loan), listOf("book", "loan"))
            testUseCases.`find account`(account.id, listOf("book", "loan"))
                .assertStatus(HttpStatusCode.OK)
                .assertJsonApiResponse(expected)
        }

    @Test
    fun `fails when finding a non existing AccountId`() =
        testApplication {
            testUseCases.`find account`(account.id)
                .assertStatus(HttpStatusCode.NotFound)
                .assertJsonApiErrorDocument(
                    jsonApiErrorDocument(
                        status = HttpStatusCode.NotFound,
                        title = "AccountNotFoundError",
                        detail = "Account ${account.id.value} not found",
                    ),
                )
        }

    private val now = LocalDateTime.now()
    private val book = BookMother.random()
    private val anotherBook = BookMother.random()

    private val account = AccountMother.random()
    private val loan = LoanMother.random(accountId = account.id, bookId = book.id)
    private val anotherLoan = LoanMother.random(accountId = account.id, bookId = anotherBook.id).finishLoan(FinishedAt(now))
}
