package com.github.caay2000.librarykata.hexagonal.context.account

import com.github.caay2000.common.test.RandomStringGenerator
import com.github.caay2000.common.test.http.assertJsonApiResponse
import com.github.caay2000.common.test.http.assertStatus
import com.github.caay2000.common.test.mock.MockDateProvider
import com.github.caay2000.common.test.mock.MockIdGenerator
import com.github.caay2000.dikt.DiKt
import com.github.caay2000.librarykata.hexagonal.common.TestUseCases
import com.github.caay2000.librarykata.hexagonal.context.account.mother.AccountListDocumentMother
import com.github.caay2000.librarykata.hexagonal.context.account.mother.AccountMother
import com.github.caay2000.librarykata.hexagonal.context.book.mother.BookMother
import com.github.caay2000.librarykata.hexagonal.context.domain.account.Email
import com.github.caay2000.librarykata.hexagonal.context.domain.account.PhoneNumber
import com.github.caay2000.librarykata.hexagonal.context.loan.mother.LoanMother
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.testApplication
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class SearchAccountControllerTest {
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
    fun `one account can be searched`() =
        testApplication {
            testUseCases.`account is created`(account)

            val expected = AccountListDocumentMother.json(account)
            testUseCases.`search account`()
                .assertStatus(HttpStatusCode.OK)
                .assertJsonApiResponse(expected)
        }

    @Test
    fun `multiple accounts can be searched`() =
        testApplication {
            testUseCases.`account is created`(account)
            testUseCases.`account is created`(anotherAccount)

            val expected = AccountListDocumentMother.json(accounts = listOf(account, anotherAccount), loans = emptyList())
            testUseCases.`search account`()
                .assertStatus(HttpStatusCode.OK)
                .assertJsonApiResponse(expected)
        }

    @Test
    fun `multiple accounts can be searched by partial phone number`() =
        testApplication {
            testUseCases.`account is created`(account)
            testUseCases.`account is created`(accountWithSimilarPhoneNumber)
            testUseCases.`account is created`(anotherAccount)

            val expected = AccountListDocumentMother.json(accounts = listOf(account, accountWithSimilarPhoneNumber), loans = emptyList())
            testUseCases.`search account by phoneNumber`(account.phoneNumber.value.take(4))
                .assertStatus(HttpStatusCode.OK)
                .assertJsonApiResponse(expected)
        }

    @Test
    fun `multiple accounts can be searched by partial email`() =
        testApplication {
            testUseCases.`account is created`(account)
            testUseCases.`account is created`(accountWithSimilarEmail)
            testUseCases.`account is created`(anotherAccount)

            val expected = AccountListDocumentMother.json(accounts = listOf(account, accountWithSimilarEmail), loans = emptyList())
            testUseCases.`search account by email`(account.email.value.substringAfter("@"))
                .assertStatus(HttpStatusCode.OK)
                .assertJsonApiResponse(expected)
        }

    @Test
    fun `multiple accounts with loans can be searched by partial email`() =
        testApplication {
            testUseCases.`account is created`(account)
            testUseCases.`book is created`(book)
            testUseCases.`loan is created`(
                id = loan.id,
                bookIsbn = book.isbn,
                accountId = account.id,
            )

            testUseCases.`account is created`(accountWithSimilarEmail)
            testUseCases.`account is created`(anotherAccount)

            val expected = AccountListDocumentMother.json(accounts = listOf(account, accountWithSimilarEmail), loans = listOf(loan))
            testUseCases.`search account by email`(account.email.value.substringAfter("@"))
                .assertStatus(HttpStatusCode.OK)
                .assertJsonApiResponse(expected)
        }

    private val account = AccountMother.random()
    private val anotherAccount = AccountMother.random()
    private val book = BookMother.random()
    private val loan = LoanMother.random(accountId = account.id, bookId = book.id)

    private val accountWithSimilarPhoneNumber =
        AccountMother.random()
            .copy(phoneNumber = PhoneNumber(account.phoneNumber.value.toInt().inc().toString()))

    private val accountWithSimilarEmail =
        AccountMother.random()
            .copy(email = Email(account.email.value.replaceBefore("@", RandomStringGenerator.randomName())))
}
