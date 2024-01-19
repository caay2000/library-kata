package com.github.caay2000.librarykata.hexagonal.context.loan

import com.github.caay2000.common.jsonapi.jsonApiErrorDocument
import com.github.caay2000.common.test.http.assertJsonApiErrorDocument
import com.github.caay2000.common.test.http.assertResponse
import com.github.caay2000.common.test.http.assertStatus
import com.github.caay2000.common.test.mock.MockDateProvider
import com.github.caay2000.common.test.mock.MockIdGenerator
import com.github.caay2000.dikt.DiKt
import com.github.caay2000.librarykata.hexagonal.common.TestUseCases
import com.github.caay2000.librarykata.hexagonal.context.account.mother.AccountMother
import com.github.caay2000.librarykata.hexagonal.context.book.mother.BookIsbnMother
import com.github.caay2000.librarykata.hexagonal.context.book.mother.BookMother
import com.github.caay2000.librarykata.hexagonal.context.domain.account.AccountId
import com.github.caay2000.librarykata.hexagonal.context.domain.book.BookId
import com.github.caay2000.librarykata.hexagonal.context.loan.mother.LoanMother
import com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.loan.serialization.toJsonApiDocument
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.testApplication
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CreateLoanControllerTest {
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
    fun `a loan can be created`() =
        testApplication {
            testUseCases.`book is created`(book)
            testUseCases.`account is created`(account)

            testUseCases.`loan is created`(loan)
                .assertStatus(HttpStatusCode.Created)
                .assertResponse(loan.toJsonApiDocument(account, book))
        }

    @Test
    fun `fails when creating a loan for a non existing account`() =
        testApplication {
            testUseCases.`book is created`(book)

            testUseCases.`loan is created`(loan)
                .assertStatus(HttpStatusCode.BadRequest)
                .assertJsonApiErrorDocument(
                    jsonApiErrorDocument(
                        status = HttpStatusCode.BadRequest,
                        title = "AccountNotFound",
                        detail = "Account ${account.id.value} not found",
                    ),
                )
        }

    @Test
    fun `fails when creating a loan for a non existing book`() =
        testApplication {
            testUseCases.`account is created`(account)

            val invalidBookIsbn = BookIsbnMother.random()
            testUseCases.`loan is created`(loan, bookIsbn = invalidBookIsbn)
                .assertStatus(HttpStatusCode.BadRequest)
                .assertJsonApiErrorDocument(
                    jsonApiErrorDocument(
                        status = HttpStatusCode.BadRequest,
                        title = "BookNotFound",
                        detail = "Book with isbn ${invalidBookIsbn.value} not found",
                    ),
                )
        }

    @Test
    fun `fails when creating a loan with an already lent book`() =
        testApplication {
            testUseCases.`account is created with a loan`(account, book, loan)

            testUseCases.`loan is created`(loan, book.isbn)
                .assertStatus(HttpStatusCode.BadRequest)
                .assertJsonApiErrorDocument(
                    jsonApiErrorDocument(
                        status = HttpStatusCode.BadRequest,
                        title = "BookNotAvailable",
                        detail = "Book with isbn ${book.isbn.value} is not available",
                    ),
                )
        }

    @Test
    fun `fails when creating a loan for an account with loan limit reached`() =
        testApplication {
            testUseCases.`multiple copies of the same book are created`(book, 6)
            testUseCases.`account is created`(account)
            repeat(5) {
                testUseCases.`loan is created`(accountId = AccountId(account.id.value), bookIsbn = book.isbn)
            }

            testUseCases.`loan is created`(loan, book.isbn)
                .assertStatus(HttpStatusCode.BadRequest)
                .assertJsonApiErrorDocument(
                    jsonApiErrorDocument(
                        status = HttpStatusCode.BadRequest,
                        title = "AccountHasTooManyLoans",
                        detail = "Account ${account.id.value} has too many loans",
                    ),
                )
        }

    @Test
    fun `an account with 5 loans can retrieve more books if finish one of the previous loans`() =
        testApplication {
            testUseCases.`multiple copies of the same book are created`(book, 5)
            testUseCases.`account is created`(account)
            repeat(5) {
                testUseCases.`loan is created`(accountId = AccountId(account.id.value), bookIsbn = book.isbn)
            }

            testUseCases.`loan is finished`(bookId = BookId(book.id.value))

            testUseCases.`loan is created`(loan, book.isbn)
                .assertStatus(HttpStatusCode.Created)
        }

    // TODO missing error tests

    private val account = AccountMother.random()
    private val book = BookMother.random()
    private val loan = LoanMother.random(bookId = book.id, accountId = account.id)
}
