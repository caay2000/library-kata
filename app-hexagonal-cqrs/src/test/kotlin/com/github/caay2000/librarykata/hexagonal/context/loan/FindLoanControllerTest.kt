package com.github.caay2000.librarykata.hexagonal.context.loan

import com.github.caay2000.common.test.http.assertJsonApiResponse
import com.github.caay2000.common.test.http.assertStatus
import com.github.caay2000.common.test.mock.MockDateProvider
import com.github.caay2000.common.test.mock.MockIdGenerator
import com.github.caay2000.dikt.DiKt
import com.github.caay2000.librarykata.hexagonal.common.TestUseCases
import com.github.caay2000.librarykata.hexagonal.context.account.mother.AccountMother
import com.github.caay2000.librarykata.hexagonal.context.book.mother.BookMother
import com.github.caay2000.librarykata.hexagonal.context.domain.book.BookAvailable
import com.github.caay2000.librarykata.hexagonal.context.domain.loan.CreatedAt
import com.github.caay2000.librarykata.hexagonal.context.domain.loan.FinishedAt
import com.github.caay2000.librarykata.hexagonal.context.loan.mother.LoanDocumentMother
import com.github.caay2000.librarykata.hexagonal.context.loan.mother.LoanMother
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.testApplication
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class FindLoanControllerTest {
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
    fun `a loan can be retrieved`() =
        testApplication {
            testUseCases.`account is created with a loan`(account, book, loan)

            val expected = LoanDocumentMother.random(loan, account, book)
            testUseCases.`find loan`(loan.id)
                .assertStatus(HttpStatusCode.OK)
                .assertJsonApiResponse(expected)
        }

    @Test
    fun `a loan can be retrieved with account and book information `() =
        testApplication {
            testUseCases.`account is created with a loan`(account, book, loan)

            val expected = LoanDocumentMother.random(loan, account, lentBook, listOf("account", "book"))
            testUseCases.`find loan`(loan.id, listOf("account", "book"))
                .assertStatus(HttpStatusCode.OK)
                .assertJsonApiResponse(expected)
        }

    @Test
    fun `a finished loan can be retrieved`() =
        testApplication {
            testUseCases.`account is created with a loan`(account, book, loan)
            testUseCases.`loan is finished`(finishedLoan)

            val expected = LoanDocumentMother.random(finishedLoan, account, book)
            testUseCases.`find loan`(finishedLoan.id)
                .assertStatus(HttpStatusCode.OK)
                .assertJsonApiResponse(expected)
        }

    @Test
    fun `a finished loan can be retrieved with account and book information `() =
        testApplication {
            testUseCases.`account is created with a loan`(account, book, loan)
            testUseCases.`loan is finished`(finishedLoan)

            val expected = LoanDocumentMother.random(finishedLoan, account, book, listOf("account", "book"))
            testUseCases.`find loan`(finishedLoan.id, listOf("account", "book"))
                .assertStatus(HttpStatusCode.OK)
                .assertJsonApiResponse(expected)
        }

    private val now = LocalDateTime.now()
    private val finishDate = now.plusDays(2)
    private val account = AccountMother.random()
    private val book = BookMother.random()
    private val lentBook = book.copy(available = BookAvailable.notAvailable())
    private val loan = LoanMother.random(bookId = book.id, accountId = account.id, createdAt = CreatedAt(now))
    private val finishedLoan = loan.copy(finishedAt = FinishedAt(finishDate))
}
