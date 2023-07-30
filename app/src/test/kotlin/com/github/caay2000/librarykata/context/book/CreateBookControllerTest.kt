package com.github.caay2000.librarykata.context.book

import com.github.caay2000.common.test.http.assertResponse
import com.github.caay2000.common.test.http.assertStatus
import com.github.caay2000.common.test.mock.MockDateProvider
import com.github.caay2000.common.test.mock.MockIdGenerator
import com.github.caay2000.dikt.DiKt
import com.github.caay2000.librarykata.common.TestUseCases
import com.github.caay2000.librarykata.context.book.mother.BookIdMother
import com.github.caay2000.librarykata.context.book.mother.BookMother
import com.github.caay2000.librarykata.context.book.primaryadapter.http.serialization.toBookByIdDocument
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.testApplication
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CreateBookControllerTest {

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
    fun `a book can be created`() = testApplication {
        testUseCases.`book is created`(book)
            .assertStatus(HttpStatusCode.Created)
            .assertResponse(book.toBookByIdDocument())
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
    fun `multiple books with same isbn will have different id`() = testApplication {
        testUseCases.`book is created`(book)
            .assertStatus(HttpStatusCode.Created)
            .assertResponse(book.toBookByIdDocument())

        testUseCases.`book is created`(differentIdBook)
            .assertStatus(HttpStatusCode.Created)
            .assertResponse(differentIdBook.toBookByIdDocument())
    }

    private val book = BookMother.random()
    private val differentIdBook = book.copy(id = BookIdMother.random())
}
