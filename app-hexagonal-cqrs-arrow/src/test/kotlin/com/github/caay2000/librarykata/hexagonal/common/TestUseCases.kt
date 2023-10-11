package com.github.caay2000.librarykata.hexagonalarrow.common

import com.github.caay2000.common.test.http.HttpDataResponse
import com.github.caay2000.common.test.mock.MockDateProvider
import com.github.caay2000.common.test.mock.MockIdGenerator
import com.github.caay2000.librarykata.hexagonalarrow.context.account.mother.AccountMother
import com.github.caay2000.librarykata.hexagonalarrow.context.book.mother.BookIdMother
import com.github.caay2000.librarykata.hexagonalarrow.context.book.mother.BookIsbnMother
import com.github.caay2000.librarykata.hexagonalarrow.context.domain.Account
import com.github.caay2000.librarykata.hexagonalarrow.context.domain.AccountId
import com.github.caay2000.librarykata.hexagonalarrow.context.domain.Book
import com.github.caay2000.librarykata.hexagonalarrow.context.domain.BookAuthor
import com.github.caay2000.librarykata.hexagonalarrow.context.domain.BookId
import com.github.caay2000.librarykata.hexagonalarrow.context.domain.BookIsbn
import com.github.caay2000.librarykata.hexagonalarrow.context.domain.BookPages
import com.github.caay2000.librarykata.hexagonalarrow.context.domain.BookPublisher
import com.github.caay2000.librarykata.hexagonalarrow.context.domain.BookTitle
import com.github.caay2000.librarykata.hexagonalarrow.context.domain.CreatedAt
import com.github.caay2000.librarykata.hexagonalarrow.context.domain.Email
import com.github.caay2000.librarykata.hexagonalarrow.context.domain.FinishedAt
import com.github.caay2000.librarykata.hexagonalarrow.context.domain.IdentityNumber
import com.github.caay2000.librarykata.hexagonalarrow.context.domain.Loan
import com.github.caay2000.librarykata.hexagonalarrow.context.domain.LoanId
import com.github.caay2000.librarykata.hexagonalarrow.context.domain.PhoneNumber
import com.github.caay2000.librarykata.hexagonalarrow.context.domain.PhonePrefix
import com.github.caay2000.librarykata.hexagonalarrow.context.loan.mother.LoanMother
import com.github.caay2000.librarykata.hexagonalarrow.context.primaryadapter.http.serialization.AccountDetailsDocument
import com.github.caay2000.librarykata.hexagonalarrow.context.primaryadapter.http.serialization.AllBooksDocument
import com.github.caay2000.librarykata.hexagonalarrow.context.primaryadapter.http.serialization.BookByIdDocument
import com.github.caay2000.librarykata.hexagonalarrow.context.primaryadapter.http.serialization.BookDocument
import com.github.caay2000.librarykata.hexagonalarrow.context.primaryadapter.http.serialization.LoanByAccountIdDocument
import com.github.caay2000.librarykata.hexagonalarrow.context.primaryadapter.http.serialization.LoanDocument
import io.ktor.server.testing.ApplicationTestBuilder
import java.time.LocalDate
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
    ): HttpDataResponse<AccountDetailsDocument> {
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
    fun `find account`(id: AccountId): HttpDataResponse<AccountDetailsDocument> = libraryClient.findAccount(id)

    context(ApplicationTestBuilder)
    fun `book is created`(
        book: Book,
        id: BookId? = null,
        isbn: BookIsbn? = null,
        title: BookTitle? = null,
        author: BookAuthor? = null,
        pages: BookPages? = null,
        publisher: BookPublisher? = null,
    ): HttpDataResponse<BookByIdDocument> {
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
    fun `find book by id`(id: BookId): HttpDataResponse<BookByIdDocument> = libraryClient.findBookById(id)

    context(ApplicationTestBuilder)
    fun `find book by isbn`(isbn: BookIsbn): HttpDataResponse<BookDocument> = libraryClient.findBookByIsbn(isbn)

    context(ApplicationTestBuilder)
    fun `search all books`(): HttpDataResponse<AllBooksDocument> = libraryClient.searchBooks()

    context(ApplicationTestBuilder)
    fun `search all loans by AccountId`(accountId: AccountId): HttpDataResponse<LoanByAccountIdDocument> =
        libraryClient.searchLoanByAccountId(accountId)

    context(ApplicationTestBuilder)
    fun `loan is created`(
        loan: Loan = LoanMother.random(),
        bookIsbn: BookIsbn? = null,
        id: LoanId? = null,
        accountId: AccountId? = null,
        createdAt: CreatedAt? = null,
    ): HttpDataResponse<LoanDocument> {
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

    private fun `id will be mocked`(id: UUID): UUID = mockIdGenerator?.mock(id).let { id }
    private fun `date will be mocked`(date: LocalDate): LocalDate = mockDateProvider?.mock(date).let { date }
    private fun `datetime will be mocked`(datetime: LocalDateTime): LocalDateTime = mockDateProvider?.mock(datetime).let { datetime }
}
