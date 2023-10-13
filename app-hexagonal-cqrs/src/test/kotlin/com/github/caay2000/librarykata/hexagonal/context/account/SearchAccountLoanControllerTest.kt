package com.github.caay2000.librarykata.hexagonal.context.account

import com.github.caay2000.common.test.http.assertJsonResponse
import com.github.caay2000.common.test.http.assertResponse
import com.github.caay2000.common.test.http.assertStatus
import com.github.caay2000.common.test.http.printJsonResponse
import com.github.caay2000.common.test.mock.MockDateProvider
import com.github.caay2000.common.test.mock.MockIdGenerator
import com.github.caay2000.dikt.DiKt
import com.github.caay2000.librarykata.hexagonal.common.TestUseCases
import com.github.caay2000.librarykata.hexagonal.context.account.mother.AccountMother
import com.github.caay2000.librarykata.hexagonal.context.book.mother.BookMother
import com.github.caay2000.librarykata.hexagonal.context.domain.AccountId
import com.github.caay2000.librarykata.hexagonal.context.domain.BookId
import com.github.caay2000.librarykata.hexagonal.context.domain.Loan
import com.github.caay2000.librarykata.hexagonal.context.loan.mother.LoanMother
import com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.serialization.toAccountDocument
import com.github.caay2000.librarykata.hexagonal.jsonMapper
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.testApplication
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

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
    fun `an account can be retrieved`() = testApplication {
        testUseCases.`account is created`(account)

        testUseCases.`find account`(account.id)
            .assertStatus(HttpStatusCode.OK)
            .assertResponse(account.toAccountDocument())
    }

    @Test
    fun `a user without loans has no loans`() = testApplication {
        testUseCases.`account is created`(account)
        testUseCases.`book is created`(anotherBook)

        testUseCases.`find account`(account.id, listOf(TestUseCases.AccountInclude.LOANS))
            .assertStatus(HttpStatusCode.OK)
            .assertJsonResponse(account.toAccountDocument())
    }

    @Test
    fun `a user with one loan retrieves it`() = testApplication {
        testUseCases.`account is created`(account)
        val bookId = testUseCases.`book is created`(book).value!!.data.id
        testUseCases.`loan is created`(
            id = loan.id,
            bookIsbn = book.isbn,
            accountId = AccountId(account.id.value),
            createdAt = loan.createdAt,
        )

        testUseCases.`find account`(account.id, listOf(TestUseCases.AccountInclude.LOANS))
            .printJsonResponse(jsonMapper)
            .assertStatus(HttpStatusCode.OK)
            .assertJsonResponse(
                account.toAccountDocument(
                    listOf(
                        Loan.create(
                            id = loan.id,
                            bookId = BookId(bookId),
                            accountId = AccountId(account.id.value),
                            createdAt = loan.createdAt,
                        ),
                    ),
                ),
                jsonMapper,
            )

//        testUseCases.`search all loans by AccountId`(account.id)
//            .assertStatus(HttpStatusCode.OK)
//            .assertJsonResponse(
//                LoanByAccountIdDocument(
//                    accountId = UUID.fromString(account.id.value),
//                    loans = listOf(book.toLoanDocument(loanId = LoanId(loan.id.value), startedAt = loan.createdAt.value)),
//                ),
//            )
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

        testUseCases.`find account`(account.id, listOf(TestUseCases.AccountInclude.LOANS))
            .assertStatus(HttpStatusCode.OK)
            .assertJsonResponse(account.toAccountDocument(), jsonMapper)
//        testUseCases.`search all loans by AccountId`(account.id)
//            .assertStatus(HttpStatusCode.OK)
//            .assertJsonResponse(
//                LoanByAccountIdDocument(
//                    accountId = UUID.fromString(account.id.value),
//                    loans = listOf(
//                        book.toLoanDocument(loanId = LoanId(loan.id.value), startedAt = loan.createdAt.value),
//                        anotherBook.toLoanDocument(
//                            loanId = LoanId(anotherLoan.id.value),
//                            startedAt = anotherLoan.createdAt.value,
//                            finishedAt = anotherLoan.finishedAt!!.value,
//                        ),
//                    ),
//                ),
//            )
    }

    private val book = BookMother.random()
    private val anotherBook = BookMother.random()

    private val account = AccountMother.random()
    private val loan = LoanMother.random()
    private val anotherLoan = LoanMother.finishedLoan()
}
