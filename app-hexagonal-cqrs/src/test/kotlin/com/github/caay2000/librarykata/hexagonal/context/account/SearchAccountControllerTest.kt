package com.github.caay2000.librarykata.hexagonal.context.account

import com.github.caay2000.common.test.RandomStringGenerator
import com.github.caay2000.common.test.http.assertJsonApiResponse
import com.github.caay2000.common.test.http.assertStatus
import com.github.caay2000.common.test.mock.MockDateProvider
import com.github.caay2000.common.test.mock.MockIdGenerator
import com.github.caay2000.dikt.DiKt
import com.github.caay2000.librarykata.hexagonal.common.TestUseCases
import com.github.caay2000.librarykata.hexagonal.context.account.mother.AccountDocumentListMother
import com.github.caay2000.librarykata.hexagonal.context.account.mother.AccountMother
import com.github.caay2000.librarykata.hexagonal.context.book.mother.BookMother
import com.github.caay2000.librarykata.hexagonal.context.domain.account.CurrentLoans
import com.github.caay2000.librarykata.hexagonal.context.domain.account.Email
import com.github.caay2000.librarykata.hexagonal.context.domain.account.PhoneNumber
import com.github.caay2000.librarykata.hexagonal.context.domain.account.TotalLoans
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

            val expected = AccountDocumentListMother.random(listOf(account))
            testUseCases.`search account`()
                .assertStatus(HttpStatusCode.OK)
                .assertJsonApiResponse(expected)
        }

    @Test
    fun `multiple accounts can be searched`() =
        testApplication {
            testUseCases.`account is created`(account)
            testUseCases.`account is created`(anotherAccount)

            val expected = AccountDocumentListMother.random(accounts = listOf(account, anotherAccount))
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

            val expected = AccountDocumentListMother.random(accounts = listOf(account, accountWithSimilarPhoneNumber))
            testUseCases.`search account by phoneNumber`(account.phoneNumber.value.take(4))
                .assertStatus(HttpStatusCode.OK)
                .assertJsonApiResponse(expected)
        }

    @Test
    fun `multiple accounts with loans can be searched by partial phone number including loan information`() =
        testApplication {
            testUseCases.`account is created with a loan`(account, book, loan)
            testUseCases.`account is created`(accountWithSimilarPhoneNumber)
            testUseCases.`account is created`(anotherAccount)

            val expectedAccount = account.copy(currentLoans = CurrentLoans(1), totalLoans = TotalLoans(1))
            val expected =
                AccountDocumentListMother.random(
                    accounts = listOf(expectedAccount, accountWithSimilarPhoneNumber),
                    loans = listOf(loan),
                    include = listOf("loan"),
                )
            testUseCases.`search account by phoneNumber`(account.phoneNumber.value.take(4), listOf("loan"))
                .assertStatus(HttpStatusCode.OK)
                .assertJsonApiResponse(expected)
        }

    @Test
    fun `multiple accounts can be searched by partial email`() =
        testApplication {
            testUseCases.`account is created`(account)
            testUseCases.`account is created`(accountWithSimilarEmail)
            testUseCases.`account is created`(anotherAccount)

            val expected = AccountDocumentListMother.random(accounts = listOf(account, accountWithSimilarEmail))
            testUseCases.`search account by email`(account.email.value.substringAfter("@"))
                .assertStatus(HttpStatusCode.OK)
                .assertJsonApiResponse(expected)
        }

    @Test
    fun `multiple accounts with loans can be searched by partial email including loan information`() =
        testApplication {
            testUseCases.`account is created with a loan`(account, book, loan)

            testUseCases.`account is created`(accountWithSimilarEmail)
            testUseCases.`account is created`(anotherAccount)

            val expectedAccount = account.copy(currentLoans = CurrentLoans(1), totalLoans = TotalLoans(1))
            val expected =
                AccountDocumentListMother.random(
                    accounts = listOf(expectedAccount, accountWithSimilarEmail),
                    loans = listOf(loan),
                    include = listOf("loan"),
                )
            testUseCases.`search account by email`(account.email.value.substringAfter("@"), listOf("loan"))
                .assertStatus(HttpStatusCode.OK)
                .assertJsonApiResponse(expected)
        }

    @Test
    fun `multiple accounts with loans can be searched including loan information`() =
        testApplication {
            testUseCases.`account is created with a loan`(account, book, loan)
            testUseCases.`account is created with a loan`(anotherAccount, anotherBook, anotherLoan)

            val expectedAccount = account.copy(currentLoans = CurrentLoans(1), totalLoans = TotalLoans(1))
            val expectedAnotherAccount = anotherAccount.copy(currentLoans = CurrentLoans(1), totalLoans = TotalLoans(1))
            val expected =
                AccountDocumentListMother.random(
                    accounts = listOf(expectedAccount, expectedAnotherAccount),
                    loans = listOf(loan, anotherLoan),
                    include = listOf("loan"),
                )

            testUseCases.`search account`(listOf("loan"))
                .assertStatus(HttpStatusCode.OK)
                .assertJsonApiResponse(expected)
        }

    @Test
    fun `no account is returned if phone number is not found`() =
        testApplication {
            val expected = AccountDocumentListMother.empty()
            testUseCases.`search account by phoneNumber`(account.phoneNumber.value.take(4))
                .assertStatus(HttpStatusCode.OK)
                .assertJsonApiResponse(expected)
        }

    @Test
    fun `no account is returned if email is not found`() =
        testApplication {
            val expected = AccountDocumentListMother.empty()
            testUseCases.`search account by email`(account.email.value.substringAfter("@"), listOf("loan"))
                .assertStatus(HttpStatusCode.OK)
                .assertJsonApiResponse(expected)
        }

    private val account = AccountMother.random()
    private val anotherAccount = AccountMother.random()
    private val book = BookMother.random()
    private val anotherBook = BookMother.random()
    private val loan = LoanMother.random(accountId = account.id, bookId = book.id)
    private val anotherLoan = LoanMother.random(accountId = anotherAccount.id, bookId = anotherBook.id)

    private val accountWithSimilarPhoneNumber =
        AccountMother.random()
            .copy(phoneNumber = PhoneNumber(account.phoneNumber.value.toInt().inc().toString()))

    private val accountWithSimilarEmail =
        AccountMother.random()
            .copy(email = Email(account.email.value.replaceBefore("@", RandomStringGenerator.randomName())))
}
