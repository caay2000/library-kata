package com.github.caay2000.librarykata.hexagonalarrow.context.account

import com.github.caay2000.common.test.http.assertJsonResponse
import com.github.caay2000.common.test.http.assertStatus
import com.github.caay2000.common.test.mock.MockDateProvider
import com.github.caay2000.common.test.mock.MockIdGenerator
import com.github.caay2000.dikt.DiKt
import com.github.caay2000.librarykata.hexagonalarrow.common.TestUseCases
import com.github.caay2000.librarykata.hexagonalarrow.context.account.mother.AccountMother
import com.github.caay2000.librarykata.hexagonalarrow.context.book.mother.BookMother
import com.github.caay2000.librarykata.hexagonalarrow.context.book.mother.LoanDocumentMother.toLoanDocument
import com.github.caay2000.librarykata.hexagonalarrow.context.domain.AccountId
import com.github.caay2000.librarykata.hexagonalarrow.context.domain.BookId
import com.github.caay2000.librarykata.hexagonalarrow.context.domain.LoanId
import com.github.caay2000.librarykata.hexagonalarrow.context.loan.mother.LoanMother
import com.github.caay2000.librarykata.hexagonalarrow.context.primaryadapter.http.serialization.LoanByAccountIdDocument
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.testApplication
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.UUID

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
    fun `a user without loans has no loans`() = testApplication {
        testUseCases.`account is created`(account)
        testUseCases.`book is created`(anotherBook)

        testUseCases.`search all loans by AccountId`(account.id)
            .assertStatus(HttpStatusCode.OK)
            .assertJsonResponse(
                LoanByAccountIdDocument(
                    accountId = UUID.fromString(account.id.value),
                    loans = emptyList(),
                ),
            )
    }

    @Test
    fun `a user with one loan retrieves it`() = testApplication {
        testUseCases.`account is created`(account)
        testUseCases.`book is created`(book)
        testUseCases.`loan is created`(
            id = loan.id,
            bookIsbn = book.isbn,
            accountId = AccountId(account.id.value),
            createdAt = loan.createdAt,
        )

        testUseCases.`search all loans by AccountId`(account.id)
            .assertStatus(HttpStatusCode.OK)
            .assertJsonResponse(
                LoanByAccountIdDocument(
                    accountId = UUID.fromString(account.id.value),
                    loans = listOf(book.toLoanDocument(loanId = LoanId(loan.id.value), startedAt = loan.createdAt.value)),
                ),
            )
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

        testUseCases.`book is created`(anotherBook)
        testUseCases.`loan is created`(
            id = anotherLoan.id,
            bookIsbn = anotherBook.isbn,
            accountId = AccountId(account.id.value),
            createdAt = anotherLoan.createdAt,
        )
        testUseCases.`loan is finished`(bookId = BookId(anotherBook.id.value), finishedAt = anotherLoan.finishedAt)

        testUseCases.`search all loans by AccountId`(account.id)
            .assertStatus(HttpStatusCode.OK)
            .assertJsonResponse(
                LoanByAccountIdDocument(
                    accountId = UUID.fromString(account.id.value),
                    loans = listOf(
                        book.toLoanDocument(loanId = LoanId(loan.id.value), startedAt = loan.createdAt.value),
                        anotherBook.toLoanDocument(
                            loanId = LoanId(anotherLoan.id.value),
                            startedAt = anotherLoan.createdAt.value,
                            finishedAt = anotherLoan.finishedAt!!.value,
                        ),
                    ),
                ),
            )
    }

    private val book = BookMother.random()
    private val anotherBook = BookMother.random()

    private val account = AccountMother.random()
    private val loan = LoanMother.random()
    private val anotherLoan = LoanMother.finishedLoan()
}
