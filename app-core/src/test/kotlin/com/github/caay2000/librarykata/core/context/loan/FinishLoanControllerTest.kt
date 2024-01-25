package com.github.caay2000.librarykata.core.context.loan

import com.github.caay2000.common.jsonapi.jsonApiErrorDocument
import com.github.caay2000.common.test.http.assertJsonApiErrorDocument
import com.github.caay2000.common.test.http.assertResponse
import com.github.caay2000.common.test.http.assertStatus
import com.github.caay2000.common.test.mock.MockDateProvider
import com.github.caay2000.common.test.mock.MockIdGenerator
import com.github.caay2000.dikt.DiKt
import com.github.caay2000.librarykata.core.common.TestUseCases
import com.github.caay2000.librarykata.core.context.account.mother.AccountMother
import com.github.caay2000.librarykata.core.context.book.mother.BookMother
import com.github.caay2000.librarykata.core.context.loan.mother.LoanMother
import com.github.caay2000.librarykata.hexagonal.context.book.domain.BookId
import com.github.caay2000.librarykata.hexagonal.context.loan.primaryadapter.http.transformer.toJsonApiDocument
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.testApplication
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class FinishLoanControllerTest {
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
    fun `a book can be returned`() =
        testApplication {
            testUseCases.`account is created with a loan`(account, book, loan)

            testUseCases.`loan is finished`(bookId = BookId(book.id.value))
                .assertStatus(HttpStatusCode.Accepted)
        }

    @Test
    fun `after finishing a book it can be lent again`() =
        testApplication {
            testUseCases.`account is created with a loan`(account, book, loan)
            testUseCases.`loan is finished`(bookId = BookId(book.id.value))

            testUseCases.`loan is created`(loan, book.isbn)
                .assertStatus(HttpStatusCode.Created)
                .assertResponse(loan.toJsonApiDocument(account, book))
        }

    @Test
    fun `fails when trying to return a not lent book`() =
        testApplication {
            testUseCases.`loan is finished`(bookId = BookId(book.id.value))
                .assertStatus(HttpStatusCode.BadRequest)
                .assertJsonApiErrorDocument(
                    jsonApiErrorDocument(
                        status = HttpStatusCode.BadRequest,
                        title = "LoanNotFound",
                        detail = "loan for book ${book.id.value} not found",
                    ),
                )
        }

    private val account = AccountMother.random()
    private val book = BookMother.random()
    private val loan = LoanMother.random(bookId = book.id, accountId = account.id)
}
