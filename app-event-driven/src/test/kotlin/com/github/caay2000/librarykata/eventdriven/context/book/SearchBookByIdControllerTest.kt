package com.github.caay2000.librarykata.eventdriven.context.book

import com.github.caay2000.common.test.http.assertResponse
import com.github.caay2000.common.test.http.assertStatus
import com.github.caay2000.common.test.mock.MockDateProvider
import com.github.caay2000.common.test.mock.MockIdGenerator
import com.github.caay2000.dikt.DiKt
import com.github.caay2000.librarykata.eventdriven.common.TestUseCases
import com.github.caay2000.librarykata.eventdriven.context.account.mother.AccountMother
import com.github.caay2000.librarykata.eventdriven.context.book.domain.BookAvailable
import com.github.caay2000.librarykata.eventdriven.context.book.mother.BookMother
import com.github.caay2000.librarykata.eventdriven.context.book.primaryadapter.http.serialization.toBookByIdDocument
import com.github.caay2000.librarykata.eventdriven.context.loan.domain.UserId
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.testApplication
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class SearchBookByIdControllerTest {

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
    fun `a book can be retrieved by Id`() = testApplication {
        testUseCases.`book is created`(book)
            .assertStatus(HttpStatusCode.Created)

        testUseCases.`find book by id`(book.id)
            .assertStatus(HttpStatusCode.OK)
            .assertResponse(book.toBookByIdDocument())
    }

    @Test
    fun `a lent book can be retrieved by Id`() = testApplication {
        testUseCases.`book is created`(book)
            .assertStatus(HttpStatusCode.Created)
        testUseCases.`account is created`(account)
            .assertStatus(HttpStatusCode.Created)
        testUseCases.`loan is created`(bookIsbn = book.isbn, userId = UserId(account.id.value))

        testUseCases.`find book by id`(book.id)
            .assertStatus(HttpStatusCode.OK)
            .assertResponse(notAvailableBook.toBookByIdDocument())
    }

    private val account = AccountMother.random()
    private val book = BookMother.random()
    private val notAvailableBook = book.copy(available = BookAvailable.notAvailable())
}
