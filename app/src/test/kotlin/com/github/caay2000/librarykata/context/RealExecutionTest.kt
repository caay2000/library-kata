package com.github.caay2000.librarykata.context

import com.github.caay2000.librarykata.common.LibraryClient
import com.github.caay2000.librarykata.context.account.domain.Account
import com.github.caay2000.librarykata.context.account.domain.AccountId
import com.github.caay2000.librarykata.context.account.mother.AccountMother
import com.github.caay2000.librarykata.context.book.domain.Book
import com.github.caay2000.librarykata.context.book.domain.BookId
import com.github.caay2000.librarykata.context.book.mother.BookMother
import com.github.caay2000.librarykata.context.loan.domain.BookIsbn
import com.github.caay2000.librarykata.context.loan.domain.Loan
import com.github.caay2000.librarykata.context.loan.domain.UserId
import io.ktor.server.testing.ApplicationTestBuilder
import io.ktor.server.testing.testApplication
import mu.KLogger
import mu.KotlinLogging
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.random.Random

class RealExecutionTest {

    private val libraryClient: LibraryClient = LibraryClient()

    private val availableBooks = mutableMapOf<Book, Int>()
    private val existingAccounts = mutableMapOf<Account, Int>()
    private val existingLoans = mutableListOf<Loan>()

    private val logger: KLogger = KotlinLogging.logger {}

    @Disabled
    @Test
    fun `real execution test`() = testApplication {
        `multiple accounts are created`(100)
        `multiple books are created`(1000)

        repeat(50000) {
            val random = Random.nextInt(1000)
            when (random) {
                in 1..399 -> startLoan()
                in 400..799 -> finishLoan()
                in 800..849 -> `create book copy`()
                in 849..949 -> `create new book`()
                in 950..999 -> `create new account`()
            }
        }
    }

    context(ApplicationTestBuilder)
    private fun startLoan() {
        logger.info { "Start new LOAN" }
        val book = availableBooks.keys.random()
        val account = existingAccounts.keys.random()
        if (existingAccounts[account]!! < 5 && availableBooks[book]!! > 0) {
            libraryClient.createLoan(BookIsbn(book.isbn.value), UserId(account.id.value))
            availableBooks[book] = availableBooks[book]!!.dec()
            existingAccounts[account] = existingAccounts[account]!!.inc()
        }
    }

    context(ApplicationTestBuilder)
    private fun finishLoan() {
        if (existingLoans.size > 10) {
            logger.info { "Finishing LOAN" }
            val loan = existingLoans.random()
            libraryClient.finishLoan(loan.bookId)
            val returnedBook = availableBooks.keys.first { it.id == BookId(loan.bookId.value) }
            val account = existingAccounts.keys.first { it.id == AccountId(loan.userId.value) }
            availableBooks[returnedBook] = availableBooks[returnedBook]!!.inc()
            existingAccounts[account] = existingAccounts[account]!!.inc()
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
            if (availableBooks.size > 10 && random < 50) {
                `create book copy`()
            } else {
                `create new book`()
            }
        }

    context(ApplicationTestBuilder)
    private fun `create new book`() =
        with(BookMother.random()) {
            logger.info { "Create new BOOK" }
            availableBooks[this] = 1
            val id = libraryClient.createBook(
                isbn = isbn,
                title = title,
                author = author,
                pages = pages,
                publisher = publisher,
            ).value!!.id
            availableBooks[this.copy(id = BookId(id))] = 0

            val size = libraryClient.searchBooks().value!!.books.size
            logger.info { "Current Total books: $size" }
        }

    context(ApplicationTestBuilder)
    private fun `create book copy`() =
        with(availableBooks.keys.random()) {
            logger.info { "Create BOOK copy" }
            libraryClient.createBook(
                isbn = isbn,
                title = title,
                author = author,
                pages = pages,
                publisher = publisher,
            )
            val book = availableBooks.keys.first { it.isbn == this.isbn }
            availableBooks[book] = availableBooks[this]!!.inc()

            val size = libraryClient.searchBooks().value!!.books.size
            logger.info { "Current Total books: $size" }
        }

    context(ApplicationTestBuilder)
    private fun `create new account`() =
        with(AccountMother.random()) {
            logger.info { "Create new ACCOUNT" }
            val id = libraryClient.createAccount(
                identityNumber = identityNumber,
                name = name,
                surname = surname,
                birthdate = birthdate,
                email = email,
                phonePrefix = phonePrefix,
                phoneNumber = phoneNumber,
            ).value!!.id
            existingAccounts[this.copy(id = AccountId(UUID.fromString(id)))] = 0
        }
}
