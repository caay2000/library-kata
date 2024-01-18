package com.github.caay2000.librarykata.hexagonal.context.book

import com.github.caay2000.common.test.http.assertJsonApiResponse
import com.github.caay2000.common.test.http.assertStatus
import com.github.caay2000.common.test.mock.MockDateProvider
import com.github.caay2000.common.test.mock.MockIdGenerator
import com.github.caay2000.dikt.DiKt
import com.github.caay2000.librarykata.hexagonal.common.TestUseCases
import com.github.caay2000.librarykata.hexagonal.context.book.mother.BookDocumentMother
import com.github.caay2000.librarykata.hexagonal.context.book.mother.BookIdMother
import com.github.caay2000.librarykata.hexagonal.context.book.mother.BookMother
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
    fun `a book can be created`() =
        testApplication {
            val expected = BookDocumentMother.random(book)
            testUseCases.`book is created`(book)
                .assertStatus(HttpStatusCode.Created)
                .assertJsonApiResponse(expected)
        }

    @Test
    fun `multiple books with same isbn will have different id`() =
        testApplication {
            val expected = BookDocumentMother.random(book)
            testUseCases.`book is created`(book)
                .assertStatus(HttpStatusCode.Created)
                .assertJsonApiResponse(expected)

            val expectedDifferentId = BookDocumentMother.random(differentIdBook)
            testUseCases.`book is created`(differentIdBook)
                .assertStatus(HttpStatusCode.Created)
                .assertJsonApiResponse(expectedDifferentId)
        }

    // TODO missing error tests

    private val book = BookMother.random()
    private val differentIdBook = book.copy(id = BookIdMother.random())
}
