package com.github.caay2000.projectskeleton.context.loan.mother

import com.github.caay2000.common.test.http.assertErrorMessage
import com.github.caay2000.common.test.http.assertResponse
import com.github.caay2000.common.test.http.assertStatus
import com.github.caay2000.common.test.mock.MockDateProvider
import com.github.caay2000.common.test.mock.MockIdGenerator
import com.github.caay2000.dikt.DiKt
import com.github.caay2000.projectskeleton.common.TestUseCases
import com.github.caay2000.projectskeleton.context.account.mother.AccountMother
import com.github.caay2000.projectskeleton.context.book.mother.BookMother
import com.github.caay2000.projectskeleton.context.loan.mother.mother.LoanMother
import com.github.caay2000.projectskeleton.context.loan.primaryadapter.http.serialization.toLoanDocument
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.testApplication
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CreateLoanControllerTest {

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
    fun `a loan can be created`() = testApplication {
        testUseCases.`book is created`(book)
            .assertStatus(HttpStatusCode.Created)

        testUseCases.`account is created`(account)
            .assertStatus(HttpStatusCode.Created)

        testUseCases.`loan is created`(loan)
            .assertStatus(HttpStatusCode.Created)
            .assertResponse(loan.toLoanDocument())
    }

    @Test
    fun `a loan for a book already lent should fail`() = testApplication {
        testUseCases.`book is created`(book)
            .assertStatus(HttpStatusCode.Created)
        testUseCases.`account is created`(account)
            .assertStatus(HttpStatusCode.Created)
        testUseCases.`loan is created`(loan)
            .assertStatus(HttpStatusCode.Created)

        testUseCases.`loan is created`(loan)
            .assertStatus(HttpStatusCode.InternalServerError)
            .assertErrorMessage("book ${book.id.value} is not available")
    }

//    @Test
//    fun `a book can be retrieved by Id`() = testApplication {
//        testUseCases.`book is created`(book)
//            .assertStatus(HttpStatusCode.Created)
//
//        testUseCases.`find book by id`(book.id)
//            .assertStatus(HttpStatusCode.OK)
//            .assertResponse(book.toBookByIdDocument())
//    }
//
//    @Test
//    fun `a book can be retrieved by Isbn`() = testApplication {
//        testUseCases.`book is created`(book)
//            .assertStatus(HttpStatusCode.Created)
//
//        testUseCases.`find book by isbn`(book.isbn)
//            .assertStatus(HttpStatusCode.OK)
//            .assertResponse(book.toBookDocument())
//    }
//
//    @Test
//    fun `multiple books with same isbn will have different id`() = testApplication {
//        testUseCases.`book is created`(book)
//            .assertStatus(HttpStatusCode.Created)
//            .assertResponse(book.toBookByIdDocument())
//
//        testUseCases.`book is created`(differentIdBook)
//            .assertStatus(HttpStatusCode.Created)
//            .assertResponse(differentIdBook.toBookByIdDocument())
//    }
//
//    @Test
//    fun `a book with multiple copies can be retrieved by Isbn`() = testApplication {
//        testUseCases.`book is created`(book)
//            .assertStatus(HttpStatusCode.Created)
//        testUseCases.`book is created`(differentIdBook)
//            .assertStatus(HttpStatusCode.Created)
//
//        testUseCases.`find book by isbn`(book.isbn)
//            .assertStatus(HttpStatusCode.OK)
//            .assertResponse(book.toBookDocument().copy(copies = 2, availableCopies = 2))
//    }

    private val account = AccountMother.random()
    private val book = BookMother.random()
    private val loan = LoanMother.random(bookId = book.id.value, userId = account.id.value)
}
