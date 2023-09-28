package com.github.caay2000.librarykata.eventdriven.context.loan

import com.github.caay2000.common.test.http.assertResponse
import com.github.caay2000.common.test.http.assertStatus
import com.github.caay2000.common.test.mock.MockDateProvider
import com.github.caay2000.common.test.mock.MockIdGenerator
import com.github.caay2000.dikt.DiKt
import com.github.caay2000.librarykata.eventdriven.common.TestUseCases
import com.github.caay2000.librarykata.eventdriven.context.account.mother.AccountMother
import com.github.caay2000.librarykata.eventdriven.context.book.mother.BookMother
import com.github.caay2000.librarykata.eventdriven.context.loan.domain.BookId
import com.github.caay2000.librarykata.eventdriven.context.loan.mother.LoanMother
import com.github.caay2000.librarykata.eventdriven.context.loan.primaryadapter.http.serialization.toLoanDocument
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.testApplication
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class FinishLoanControllerTest {

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
    fun `a book can be returned`() = testApplication {
        testUseCases.`book is created`(book)
            .assertStatus(HttpStatusCode.Created)

        testUseCases.`account is created`(account)
            .assertStatus(HttpStatusCode.Created)

        testUseCases.`loan is created`(loan, book.isbn)
            .assertStatus(HttpStatusCode.Created)
            .assertResponse(loan.toLoanDocument())

        testUseCases.`loan is finished`(bookId = BookId(book.id.value))
            .assertStatus(HttpStatusCode.Accepted)
    }

    private val account = AccountMother.random()
    private val book = BookMother.random()
    private val loan = LoanMother.random(bookId = book.id.value, userId = account.id.value)
}
