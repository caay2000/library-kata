package com.github.caay2000.librarykata.core.context.book

import com.github.caay2000.common.jsonapi.jsonApiErrorDocument
import com.github.caay2000.common.test.http.assertJsonApiErrorDocument
import com.github.caay2000.common.test.http.assertJsonApiResponse
import com.github.caay2000.common.test.http.assertStatus
import com.github.caay2000.common.test.mock.MockDateProvider
import com.github.caay2000.common.test.mock.MockIdGenerator
import com.github.caay2000.dikt.DiKt
import com.github.caay2000.librarykata.core.common.TestUseCases
import com.github.caay2000.librarykata.core.context.book.mother.BookDocumentMother
import com.github.caay2000.librarykata.core.context.book.mother.BookIdMother
import com.github.caay2000.librarykata.core.context.book.mother.BookMother
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

    @Test
    fun `fails when creating a book with an existing BookId`() =
        testApplication {
            testUseCases.`book is created`(book)
            testUseCases.`book is created`(book)
                .assertStatus(HttpStatusCode.BadRequest)
                .assertJsonApiErrorDocument(
                    jsonApiErrorDocument(
                        status = HttpStatusCode.BadRequest,
                        title = "BookAlreadyExists",
                        detail = "Book with id ${book.id.value} already exists",
                    ),
                )
        }

    private val book = BookMother.random()
    private val differentIdBook = book.copy(id = BookIdMother.random())
}
