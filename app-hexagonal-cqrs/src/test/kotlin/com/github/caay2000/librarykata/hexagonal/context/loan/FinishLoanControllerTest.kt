package com.github.caay2000.librarykata.hexagonal.context.loan

import com.github.caay2000.common.test.http.assertResponse
import com.github.caay2000.common.test.http.assertStatus
import com.github.caay2000.common.test.mock.MockDateProvider
import com.github.caay2000.common.test.mock.MockIdGenerator
import com.github.caay2000.dikt.DiKt
import com.github.caay2000.librarykata.hexagonal.common.TestUseCases
import com.github.caay2000.librarykata.hexagonal.context.account.mother.AccountMother
import com.github.caay2000.librarykata.hexagonal.context.book.mother.BookMother
import com.github.caay2000.librarykata.hexagonal.context.domain.book.BookId
import com.github.caay2000.librarykata.hexagonal.context.loan.mother.LoanMother
import com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.loan.serialization.toJsonApiDocument
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
            testUseCases.`book is created`(book)
                .assertStatus(HttpStatusCode.Created)

            testUseCases.`account is created`(account)
                .assertStatus(HttpStatusCode.Created)

            testUseCases.`loan is created`(loan, book.isbn)
                .assertStatus(HttpStatusCode.Created)
                .assertResponse(loan.toJsonApiDocument(account, book, emptyList()))

            testUseCases.`loan is finished`(bookId = BookId(book.id.value))
                .assertStatus(HttpStatusCode.Accepted)
        }

    @Test
    fun `after finishing a book it can be lended again`() =
        testApplication {
            testUseCases.`book is created`(book)
                .assertStatus(HttpStatusCode.Created)

            testUseCases.`account is created`(account)
                .assertStatus(HttpStatusCode.Created)

            testUseCases.`loan is created`(loan, book.isbn)
                .assertStatus(HttpStatusCode.Created)
                .assertResponse(loan.toJsonApiDocument(account, book, emptyList()))

            testUseCases.`loan is finished`(bookId = BookId(book.id.value))
                .assertStatus(HttpStatusCode.Accepted)

            testUseCases.`loan is created`(loan, book.isbn)
                .assertStatus(HttpStatusCode.Created)
                .assertResponse(loan.toJsonApiDocument(account, book, emptyList()))
        }

    private val account = AccountMother.random()
    private val book = BookMother.random()
    private val loan = LoanMother.random(bookId = book.id, accountId = account.id)
}
