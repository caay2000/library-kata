package com.github.caay2000.librarykata.core.context.loan

import com.github.caay2000.common.test.awaitAssertion
import com.github.caay2000.common.test.http.assertJsonApiResponse
import com.github.caay2000.common.test.http.assertStatus
import com.github.caay2000.common.test.mock.MockDateProvider
import com.github.caay2000.common.test.mock.MockIdGenerator
import com.github.caay2000.dikt.DiKt
import com.github.caay2000.librarykata.core.common.TestUseCases
import com.github.caay2000.librarykata.core.context.account.mother.AccountMother
import com.github.caay2000.librarykata.core.context.book.mother.BookIdMother
import com.github.caay2000.librarykata.core.context.book.mother.BookMother
import com.github.caay2000.librarykata.core.context.loan.mother.LoanDocumentListMother
import com.github.caay2000.librarykata.core.context.loan.mother.LoanMother
import com.github.caay2000.librarykata.hexagonal.context.account.domain.CurrentLoans
import com.github.caay2000.librarykata.hexagonal.context.account.domain.TotalLoans
import com.github.caay2000.librarykata.hexagonal.context.loan.domain.CreatedAt
import com.github.caay2000.librarykata.hexagonal.context.loan.domain.FinishedAt
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.testApplication
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class SearchLoanControllerTest {
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
    fun `all loan can be retrieved`() =
        testApplication {
            testUseCases.`account is created with a loan`(account, book, loan)
            testUseCases.`book is created`(sameBook)
            testUseCases.`loan is created`(finishedLoan)
            testUseCases.`loan is finished`(finishedLoan)
            testUseCases.`account is created with a loan`(anotherAccount, anotherBook, anotherLoan)

            val expected =
                LoanDocumentListMother.random(
                    loans = listOf(loan, finishedLoan, anotherLoan),
                    accounts = listOf(account, anotherAccount),
                    books = listOf(book, sameBook, anotherBook),
                )
            awaitAssertion {
                testUseCases.`search loan`()
                    .assertStatus(HttpStatusCode.OK)
                    .assertJsonApiResponse(expected)
            }
        }

    @Test
    fun `all loan can be retrieved with account and book information`() =
        testApplication {
            testUseCases.`account is created with a loan`(account, book, loan)
            testUseCases.`book is created`(sameBook)
            testUseCases.`loan is created`(finishedLoan)
            testUseCases.`loan is finished`(finishedLoan)
            testUseCases.`account is created with a loan`(anotherAccount, anotherBook, anotherLoan)

            val expectedAccount = account.copy(currentLoans = CurrentLoans(1), totalLoans = TotalLoans(2))
            val expectedAnotherAccount = anotherAccount.copy(currentLoans = CurrentLoans(1), totalLoans = TotalLoans(1))
            val expected =
                LoanDocumentListMother.random(
                    loans = listOf(loan, finishedLoan, anotherLoan),
                    accounts = listOf(expectedAccount, expectedAnotherAccount),
                    books = listOf(book.unavailable(), sameBook, anotherBook.unavailable()),
                    include = listOf("account", "book"),
                )
            awaitAssertion {
                testUseCases.`search loan`(listOf("account", "book"))
                    .assertStatus(HttpStatusCode.OK)
                    .assertJsonApiResponse(expected)
            }
        }

    private val now = LocalDateTime.now()
    private val finishDate = now.plusDays(2)

    private val account = AccountMother.random()
    private val anotherAccount = AccountMother.random()
    private val book = BookMother.random()
    private val sameBook = book.copy(id = BookIdMother.random())
    private val anotherBook = BookMother.random()

    private val loan = LoanMother.random(bookId = book.id, accountId = account.id, createdAt = CreatedAt(now))
    private val finishedLoan = LoanMother.random(bookId = sameBook.id, accountId = account.id, createdAt = CreatedAt(now), finishedAt = FinishedAt(finishDate))
    private val anotherLoan = LoanMother.random(bookId = anotherBook.id, accountId = anotherAccount.id, createdAt = CreatedAt(now))
}
