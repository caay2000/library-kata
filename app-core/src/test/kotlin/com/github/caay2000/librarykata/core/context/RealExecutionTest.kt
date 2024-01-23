package com.github.caay2000.librarykata.core.context

import com.github.caay2000.librarykata.core.common.LibraryClient
import com.github.caay2000.librarykata.core.context.account.mother.AccountMother
import com.github.caay2000.librarykata.core.context.book.mother.BookMother
import com.github.caay2000.librarykata.hexagonal.context.domain.account.Account
import com.github.caay2000.librarykata.hexagonal.context.domain.account.AccountId
import com.github.caay2000.librarykata.hexagonal.context.domain.book.Book
import com.github.caay2000.librarykata.hexagonal.context.domain.book.BookId
import com.github.caay2000.librarykata.hexagonal.context.domain.book.BookIsbn
import com.github.caay2000.librarykata.hexagonal.context.domain.loan.CreatedAt
import com.github.caay2000.librarykata.hexagonal.context.domain.loan.Loan
import com.github.caay2000.librarykata.hexagonal.context.domain.loan.LoanId
import io.ktor.server.testing.ApplicationTestBuilder
import io.ktor.server.testing.testApplication
import mu.KLogger
import mu.KotlinLogging
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import kotlin.random.Random

class RealExecutionTest {
    private val libraryClient: LibraryClient = LibraryClient()

    private val books = mutableListOf<Book>()
    private val availableBooks = mutableMapOf<BookIsbn, Int>()
    private val existingAccounts = mutableMapOf<Account, Int>()
    private val existingLoans = mutableMapOf<BookId, Loan>()

    private val logger: KLogger = KotlinLogging.logger {}

    private val map = mutableMapOf<String, Int>()

    @Disabled
    @Test
    fun `real execution test`() =
        testApplication {
            `multiple accounts are created`(20)
            `multiple books are created`(100)

            repeat(10000) {
                val random = Random.nextInt(1000)
                when (random) {
                    in 1..349 -> startLoan()
                    in 350..699 -> finishLoan()
                    in 700..799 -> `create book copy`()
                    in 800..899 -> `create new book`()
                    in 900..999 -> `create new account`()
                }
            }

            logger.info { map }
        }

    context(ApplicationTestBuilder)
    private fun startLoan() {
        val book = books.random()
        val account = existingAccounts.keys.random()
        val available = libraryClient.findBook(book.id, emptyList()).value!!.data.attributes.available
        if (available && existingAccounts[account]!! < 5 && availableBooks[book.isbn]!! > 0) {
            logger.info { "Start new LOAN, book[$book], account[$account]" }
            val jsonApiDocument = libraryClient.createLoan(book.isbn, account.id).value!!
            availableBooks[book.isbn] = availableBooks[book.isbn]!!.dec()
            existingAccounts[account] = existingAccounts[account]!!.inc()
            existingLoans[book.id] =
                Loan(
                    id = LoanId(jsonApiDocument.data.id),
                    bookId = book.id,
                    accountId = account.id,
                    createdAt = CreatedAt(LocalDateTime.now()),
                    finishedAt = null,
                )
            map["startLoan"] = map.getOrDefault("startLoan", 0) + 1
        }
    }

    context(ApplicationTestBuilder)
    private fun finishLoan() {
        if (existingLoans.size > 10) {
            val loan = existingLoans.values.random()
            logger.info { "Finishing LOAN, loan[$loan]" }
            libraryClient.finishLoan(loan.bookId)
            val returnedBook = books.first { it.id == loan.bookId }
            val account = existingAccounts.keys.first { it.id == loan.accountId }
            availableBooks[returnedBook.isbn] = availableBooks[returnedBook.isbn]!!.inc()
            existingAccounts[account] = existingAccounts[account]!!.dec()
            existingLoans.remove(returnedBook.id)
            map["finishLoan"] = map.getOrDefault("finishLoan", 0) + 1
        }
    }

    context(ApplicationTestBuilder)
    private fun `multiple accounts are created`(size: Int = 100) =
        repeat(size) {
            `create new account`()
        }

    context(ApplicationTestBuilder)
    private fun `multiple books are created`(size: Int = 1000) =
        repeat(size) {
            val random = Random.nextInt(1000)
            if (books.size > 10 && random < 50) {
                `create book copy`()
            } else {
                `create new book`()
            }
        }

    context(ApplicationTestBuilder)
    private fun `create new book`() =
        with(BookMother.random()) {
            logger.info { "Create new BOOK" }
            val id =
                libraryClient.createBook(
                    isbn = isbn,
                    title = title,
                    author = author,
                    pages = pages,
                    publisher = publisher,
                ).value!!.data.id
            books.add(this.copy(id = BookId(id)))
            availableBooks[this.isbn] = availableBooks.getOrDefault(this.isbn, 0) + 1

            map["createBook"] = map.getOrDefault("createBook", 0) + 1
            val size = libraryClient.searchBook().value!!.data.size
            logger.info { "Current Total books: $size" }
        }

    context(ApplicationTestBuilder)
    private fun `create book copy`() =
        with(books.random()) {
            logger.info { "Create BOOK copy" }
            libraryClient.createBook(
                isbn = isbn,
                title = title,
                author = author,
                pages = pages,
                publisher = publisher,
            )
            val book = books.first { it.isbn == this.isbn }
            availableBooks[book.isbn] = availableBooks[book.isbn]!!.inc()

            map["createBookCopy"] = map.getOrDefault("createBookCopy", 0) + 1
            val size = libraryClient.searchBook().value!!.data.size
            logger.info { "Current Total books: $size" }
        }

    context(ApplicationTestBuilder)
    private fun `create new account`() =
        with(AccountMother.random()) {
            logger.info { "Create new ACCOUNT" }
            val id =
                libraryClient.createAccount(
                    identityNumber = identityNumber,
                    name = name,
                    surname = surname,
                    birthdate = birthdate,
                    email = email,
                    phonePrefix = phone.prefix,
                    phoneNumber = phone.number,
                ).value!!.data.id
            existingAccounts[this.copy(id = AccountId(id))] = 0

            map["createAccount"] = map.getOrDefault("createAccount", 0) + 1
        }
}
