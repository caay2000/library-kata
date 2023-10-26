package com.github.caay2000.librarykata.hexagonal.common

import com.github.caay2000.common.jsonapi.JsonApiDocument
import com.github.caay2000.common.jsonapi.JsonApiListDocument
import com.github.caay2000.common.jsonapi.context.account.AccountResource
import com.github.caay2000.common.jsonapi.context.book.BookByIdResource
import com.github.caay2000.common.jsonapi.context.book.BookByIsbnResource
import com.github.caay2000.common.jsonapi.context.loan.LoanResource
import com.github.caay2000.common.test.http.HttpDataResponse
import com.github.caay2000.common.test.mock.MockDateProvider
import com.github.caay2000.common.test.mock.MockIdGenerator
import com.github.caay2000.librarykata.hexagonal.context.account.mother.AccountMother
import com.github.caay2000.librarykata.hexagonal.context.book.mother.BookIdMother
import com.github.caay2000.librarykata.hexagonal.context.book.mother.BookIsbnMother
import com.github.caay2000.librarykata.hexagonal.context.domain.account.Account
import com.github.caay2000.librarykata.hexagonal.context.domain.account.AccountId
import com.github.caay2000.librarykata.hexagonal.context.domain.account.Email
import com.github.caay2000.librarykata.hexagonal.context.domain.account.IdentityNumber
import com.github.caay2000.librarykata.hexagonal.context.domain.account.PhoneNumber
import com.github.caay2000.librarykata.hexagonal.context.domain.account.PhonePrefix
import com.github.caay2000.librarykata.hexagonal.context.domain.book.Book
import com.github.caay2000.librarykata.hexagonal.context.domain.book.BookAuthor
import com.github.caay2000.librarykata.hexagonal.context.domain.book.BookId
import com.github.caay2000.librarykata.hexagonal.context.domain.book.BookIsbn
import com.github.caay2000.librarykata.hexagonal.context.domain.book.BookPages
import com.github.caay2000.librarykata.hexagonal.context.domain.book.BookPublisher
import com.github.caay2000.librarykata.hexagonal.context.domain.book.BookTitle
import com.github.caay2000.librarykata.hexagonal.context.domain.loan.CreatedAt
import com.github.caay2000.librarykata.hexagonal.context.domain.loan.FinishedAt
import com.github.caay2000.librarykata.hexagonal.context.domain.loan.Loan
import com.github.caay2000.librarykata.hexagonal.context.domain.loan.LoanId
import com.github.caay2000.librarykata.hexagonal.context.loan.mother.LoanMother
import io.ktor.server.testing.ApplicationTestBuilder
import java.time.LocalDateTime
import java.util.UUID

class TestUseCases(
    private val libraryClient: LibraryClient = LibraryClient(),
    private val mockIdGenerator: MockIdGenerator? = null,
    private val mockDateProvider: MockDateProvider? = null,
) {

    context(ApplicationTestBuilder)
    fun `account is created`(
        account: Account = AccountMother.random(),
        accountId: AccountId? = null,
        identityNumber: IdentityNumber? = null,
        email: Email? = null,
        phonePrefix: PhonePrefix? = null,
        phoneNumber: PhoneNumber? = null,
    ): HttpDataResponse<JsonApiDocument<AccountResource>> {
        val id = accountId?.value ?: account.id.value
        `id will be mocked`(UUID.fromString(id))
        `datetime will be mocked`(account.registerDate.value)
        return libraryClient.createAccount(
            identityNumber = identityNumber ?: account.identityNumber,
            name = account.name,
            surname = account.surname,
            birthdate = account.birthdate,
            email = email ?: account.email,
            phonePrefix = phonePrefix ?: account.phonePrefix,
            phoneNumber = phoneNumber ?: account.phoneNumber,
        )
    }

    context(ApplicationTestBuilder)
    fun `find account`(
        id: AccountId,
        include: List<AccountInclude> = emptyList(),
    ): HttpDataResponse<JsonApiDocument<AccountResource>> =
        libraryClient.findAccount(id, include)

    context(ApplicationTestBuilder)
    fun `search account`(): HttpDataResponse<JsonApiListDocument<AccountResource>> =
        libraryClient.searchAccount()

    context(ApplicationTestBuilder)
    fun `search account by phoneNumber`(phoneNumber: String): HttpDataResponse<JsonApiListDocument<AccountResource>> =
        libraryClient.searchAccountByPhoneNumber(phoneNumber)

    context(ApplicationTestBuilder)
    fun `search account by email`(email: String): HttpDataResponse<JsonApiListDocument<AccountResource>> =
        libraryClient.searchAccountByEmail(email)

    context(ApplicationTestBuilder)
    fun `book is created`(
        book: Book,
        id: BookId? = null,
        isbn: BookIsbn? = null,
        title: BookTitle? = null,
        author: BookAuthor? = null,
        pages: BookPages? = null,
        publisher: BookPublisher? = null,
    ): HttpDataResponse<JsonApiDocument<BookByIdResource>> {
        `id will be mocked`(UUID.fromString(id?.value ?: book.id.value))
        return libraryClient.createBook(
            isbn = isbn ?: book.isbn,
            title = title ?: book.title,
            author = author ?: book.author,
            pages = pages ?: book.pages,
            publisher = publisher ?: book.publisher,
        )
    }

    context(ApplicationTestBuilder)
    fun `multiple copies of the same book are created`(book: Book, copies: Int) {
        repeat(copies) { index ->
            if (index == 1) {
                `book is created`(book)
            } else {
                `book is created`(book, id = BookIdMother.random())
            }
        }
    }

    context(ApplicationTestBuilder)
    fun `find book by id`(id: BookId): HttpDataResponse<JsonApiDocument<BookByIdResource>> = libraryClient.findBookById(id)

    context(ApplicationTestBuilder)
    fun `find book by isbn`(isbn: BookIsbn): HttpDataResponse<JsonApiListDocument<BookByIsbnResource>> = libraryClient.findBookByIsbn(isbn)

    context(ApplicationTestBuilder)
    fun `search book`(): HttpDataResponse<JsonApiListDocument<BookByIsbnResource>> = libraryClient.searchBook()

    context(ApplicationTestBuilder)
    fun `loan is created`(
        loan: Loan = LoanMother.random(),
        bookIsbn: BookIsbn? = null,
        id: LoanId? = null,
        accountId: AccountId? = null,
        createdAt: CreatedAt? = null,
    ): HttpDataResponse<JsonApiDocument<LoanResource>> {
        `id will be mocked`(UUID.fromString(id?.value ?: loan.id.value))
        `datetime will be mocked`(createdAt?.value ?: loan.createdAt.value)
        return libraryClient.createLoan(
            bookIsbn = BookIsbn(bookIsbn?.value ?: BookIsbnMother.random().value),
            accountId = accountId ?: loan.accountId,
        )
    }

    context(ApplicationTestBuilder)
    fun `loan is finished`(
        loan: Loan = LoanMother.random(),
        bookId: BookId? = null,
        finishedAt: FinishedAt? = null,
    ): HttpDataResponse<Unit> {
        val finishDateTime = finishedAt?.value ?: loan.finishedAt?.value ?: LocalDateTime.now()
        `datetime will be mocked`(finishDateTime)
        return libraryClient.finishLoan(bookId = bookId ?: loan.bookId)
    }

    enum class AccountInclude { LOANS }

    private fun `id will be mocked`(id: UUID): UUID = mockIdGenerator?.mock(id).let { id }
    private fun `datetime will be mocked`(datetime: LocalDateTime): LocalDateTime = mockDateProvider?.mock(datetime).let { datetime }
}
