package com.github.caay2000.librarykata.hexagonal.context.account

import com.github.caay2000.common.test.http.assertJsonResponse
import com.github.caay2000.common.test.http.assertStatus
import com.github.caay2000.common.test.mock.MockDateProvider
import com.github.caay2000.common.test.mock.MockIdGenerator
import com.github.caay2000.dikt.DiKt
import com.github.caay2000.librarykata.hexagonal.common.TestUseCases
import com.github.caay2000.librarykata.hexagonal.context.account.mother.AccountDocumentMother
import com.github.caay2000.librarykata.hexagonal.context.account.mother.AccountMother
import com.github.caay2000.librarykata.hexagonal.context.book.mother.BookMother
import com.github.caay2000.librarykata.hexagonal.context.domain.AccountId
import com.github.caay2000.librarykata.hexagonal.context.domain.BookId
import com.github.caay2000.librarykata.hexagonal.context.loan.mother.LoanMother
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.testApplication
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class SearchAccountLoanControllerTest {

    private val mockIdGenerator = MockIdGenerator()
    private val mockDateProvider = MockDateProvider()
    private val testUseCases = TestUseCases(
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
    fun `an account can be retrieved`() = testApplication {
        testUseCases.`account is created`(account)

        val expected = AccountDocumentMother.json(account)
        testUseCases.`find account`(account.id)
            .assertStatus(HttpStatusCode.OK)
            .assertJsonResponse(expected)
    }

    @Test
    fun `a user without loans has no loans`() = testApplication {
        testUseCases.`account is created`(account)
        testUseCases.`book is created`(anotherBook)

        val expected = AccountDocumentMother.json(account)
        testUseCases.`find account`(account.id, listOf(TestUseCases.AccountInclude.LOANS))
            .assertStatus(HttpStatusCode.OK)
            .assertJsonResponse(expected)
    }

    @Test
    fun `a user with one loan retrieves it`() = testApplication {
        testUseCases.`account is created`(account)
        testUseCases.`book is created`(book).value!!.data.id
        testUseCases.`loan is created`(
            id = loan.id,
            bookIsbn = book.isbn,
            accountId = AccountId(account.id.value),
            createdAt = loan.createdAt,
        )
        val loan = loan.copy(accountId = account.id, bookId = book.id)

        val expected = AccountDocumentMother.json(account, loan)
        testUseCases.`find account`(account.id)
            .assertStatus(HttpStatusCode.OK)
            .assertJsonResponse(expected)
    }

    @Test
    fun `a user with one loan retrieves it with included information`() = testApplication {
        testUseCases.`account is created`(account)
        testUseCases.`book is created`(book)
        testUseCases.`loan is created`(
            id = loan.id,
            bookIsbn = book.isbn,
            accountId = account.id,
            createdAt = loan.createdAt,
        )
        val loan = loan.copy(accountId = account.id, bookId = book.id)

        val expected = AccountDocumentMother.json(account, listOf(loan), listOf("loans"))
        testUseCases.`find account`(account.id, listOf(TestUseCases.AccountInclude.LOANS))
            .assertStatus(HttpStatusCode.OK)
            .assertJsonResponse(expected)
    }

    @Test
    fun `a user with multiple loans retrieves them`() = testApplication {
        testUseCases.`account is created`(account)
        testUseCases.`book is created`(book)
        testUseCases.`loan is created`(
            id = loan.id,
            bookIsbn = book.isbn,
            accountId = AccountId(account.id.value),
            createdAt = loan.createdAt,
        )
        val loan = loan.copy(accountId = account.id, bookId = book.id)

        testUseCases.`book is created`(anotherBook)
        testUseCases.`loan is created`(
            id = anotherLoan.id,
            bookIsbn = anotherBook.isbn,
            accountId = AccountId(account.id.value),
            createdAt = anotherLoan.createdAt,
        )
        testUseCases.`loan is finished`(bookId = BookId(anotherBook.id.value), finishedAt = anotherLoan.finishedAt)
        val anotherLoan = anotherLoan.copy(accountId = account.id, bookId = anotherBook.id)

        val expected = AccountDocumentMother.json(account, listOf(loan, anotherLoan), listOf("loans"))
        testUseCases.`find account`(account.id, listOf(TestUseCases.AccountInclude.LOANS))
            .assertStatus(HttpStatusCode.OK)
            .assertJsonResponse(expected)
    }

    private val book = BookMother.random()
    private val anotherBook = BookMother.random()

    private val account = AccountMother.random()
    private val loan = LoanMother.random()
    private val anotherLoan = LoanMother.finishedLoan()
}
