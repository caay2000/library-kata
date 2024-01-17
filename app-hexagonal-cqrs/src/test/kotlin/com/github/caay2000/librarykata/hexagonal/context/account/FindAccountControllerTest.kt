package com.github.caay2000.librarykata.hexagonal.context.account

import com.github.caay2000.common.test.http.assertJsonApiResponse
import com.github.caay2000.common.test.http.assertStatus
import com.github.caay2000.common.test.mock.MockDateProvider
import com.github.caay2000.common.test.mock.MockIdGenerator
import com.github.caay2000.dikt.DiKt
import com.github.caay2000.librarykata.hexagonal.common.TestUseCases
import com.github.caay2000.librarykata.hexagonal.context.account.mother.AccountDocumentMother
import com.github.caay2000.librarykata.hexagonal.context.account.mother.AccountMother
import com.github.caay2000.librarykata.hexagonal.context.book.mother.BookMother
import com.github.caay2000.librarykata.hexagonal.context.domain.account.AccountId
import com.github.caay2000.librarykata.hexagonal.context.domain.book.BookId
import com.github.caay2000.librarykata.hexagonal.context.domain.loan.FinishedAt
import com.github.caay2000.librarykata.hexagonal.context.loan.mother.LoanMother
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
            testUseCases.`book is created`(anotherBook)

            val expected = AccountDocumentMother.random(account, emptyList())
            testUseCases.`find account`(account.id, listOf("loan"))
                .assertStatus(HttpStatusCode.OK)
                .assertJsonApiResponse(expected)
        }

    @Test
    fun `a user with one loan retrieves it`() =
        testApplication {
            testUseCases.`account is created`(account)
            testUseCases.`book is created`(book).value!!.data.id
            testUseCases.`loan is created`(
                id = loan.id,
                bookIsbn = book.isbn,
                accountId = AccountId(account.id.value),
                createdAt = loan.createdAt,
            )

            val expected = AccountDocumentMother.random(account, listOf(loan))
            testUseCases.`find account`(account.id)
                .assertStatus(HttpStatusCode.OK)
                .assertJsonApiResponse(expected)
        }

    @Test
    fun `a user with one loan retrieves it including loan information`() =
        testApplication {
            testUseCases.`account is created`(account)
            testUseCases.`book is created`(book)
            testUseCases.`loan is created`(
                id = loan.id,
                bookIsbn = book.isbn,
                accountId = account.id,
                createdAt = loan.createdAt,
            )

            val expected = AccountDocumentMother.random(account, listOf(loan), listOf("loan"))
            testUseCases.`find account`(account.id, listOf("loan"))
                .assertStatus(HttpStatusCode.OK)
                .assertJsonApiResponse(expected)
        }

    @Test
    fun `a user with multiple loans retrieves them`() =
        testApplication {
            testUseCases.`account is created`(account)
            testUseCases.`book is created`(book)
            testUseCases.`loan is created`(
                id = loan.id,
                bookIsbn = book.isbn,
                accountId = AccountId(account.id.value),
                createdAt = loan.createdAt,
            )

            testUseCases.`book is created`(anotherBook)
            testUseCases.`loan is created`(
                id = anotherLoan.id,
                bookIsbn = anotherBook.isbn,
                accountId = AccountId(account.id.value),
                createdAt = anotherLoan.createdAt,
            )
            testUseCases.`loan is finished`(bookId = BookId(anotherBook.id.value), finishedAt = anotherLoan.finishedAt)

            val expected = AccountDocumentMother.random(account, listOf(loan, anotherLoan), listOf("loan"))
            testUseCases.`find account`(account.id, listOf("loan"))
                .assertStatus(HttpStatusCode.OK)
                .assertJsonApiResponse(expected)
        }

    private val now = LocalDateTime.now()
    private val book = BookMother.random()
    private val anotherBook = BookMother.random()

    private val account = AccountMother.random()
    private val loan = LoanMother.random(accountId = account.id, bookId = book.id)
    private val anotherLoan = LoanMother.random(accountId = account.id, bookId = anotherBook.id).finishLoan(FinishedAt(now))
}
