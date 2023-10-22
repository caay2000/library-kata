package com.github.caay2000.librarykata.hexagonal.common

import com.github.caay2000.common.http.ContentType
import com.github.caay2000.common.http.ErrorResponseDocument
import com.github.caay2000.common.jsonapi.JsonApiDocument
import com.github.caay2000.common.jsonapi.JsonApiListDocument
import com.github.caay2000.common.jsonapi.JsonApiRequestDocument
import com.github.caay2000.common.jsonapi.context.account.AccountRequestResource
import com.github.caay2000.common.jsonapi.context.account.AccountResource
import com.github.caay2000.common.jsonapi.context.book.BookByIdResource
import com.github.caay2000.common.jsonapi.context.book.BookByIsbnResource
import com.github.caay2000.common.jsonapi.context.book.BookRequestResource
import com.github.caay2000.common.jsonapi.context.loan.LoanRequestResource
import com.github.caay2000.common.jsonapi.context.loan.LoanResource
import com.github.caay2000.common.test.http.HttpDataResponse
import com.github.caay2000.librarykata.hexagonal.configuration.jsonMapper
import com.github.caay2000.librarykata.hexagonal.context.domain.AccountId
import com.github.caay2000.librarykata.hexagonal.context.domain.Birthdate
import com.github.caay2000.librarykata.hexagonal.context.domain.BookAuthor
import com.github.caay2000.librarykata.hexagonal.context.domain.BookId
import com.github.caay2000.librarykata.hexagonal.context.domain.BookIsbn
import com.github.caay2000.librarykata.hexagonal.context.domain.BookPages
import com.github.caay2000.librarykata.hexagonal.context.domain.BookPublisher
import com.github.caay2000.librarykata.hexagonal.context.domain.BookTitle
import com.github.caay2000.librarykata.hexagonal.context.domain.Email
import com.github.caay2000.librarykata.hexagonal.context.domain.IdentityNumber
import com.github.caay2000.librarykata.hexagonal.context.domain.Name
import com.github.caay2000.librarykata.hexagonal.context.domain.PhoneNumber
import com.github.caay2000.librarykata.hexagonal.context.domain.PhonePrefix
import com.github.caay2000.librarykata.hexagonal.context.domain.Surname
import com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.serialization.LoanByAccountIdDocument
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.contentType
import io.ktor.server.testing.ApplicationTestBuilder
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import mu.KLogger
import mu.KotlinLogging

class LibraryClient {

    private val logger: KLogger = KotlinLogging.logger {}

    context(ApplicationTestBuilder)
    fun createAccount(
        identityNumber: IdentityNumber,
        name: Name,
        surname: Surname,
        birthdate: Birthdate,
        email: Email,
        phonePrefix: PhonePrefix,
        phoneNumber: PhoneNumber,
    ): HttpDataResponse<JsonApiDocument<AccountResource>> =
        runBlocking {
            val request = JsonApiRequestDocument(
                data = AccountRequestResource(
                    attributes = AccountRequestResource.Attributes(
                        identityNumber = identityNumber.value,
                        name = name.value,
                        surname = surname.value,
                        birthdate = birthdate.value,
                        email = email.value,
                        phonePrefix = phonePrefix.value,
                        phoneNumber = phoneNumber.value,
                    ),
                ),
            )
            val jsonRequest = jsonMapper.encodeToString<JsonApiRequestDocument<AccountRequestResource>>(request)
            logger.trace { "CreateAccount Request: $jsonRequest" }
            client.post("/account") {
                setBody(jsonRequest)
                contentType(ContentType.JsonApi)
            }.toHttpDataResponse()
        }

    context(ApplicationTestBuilder)
    fun findAccount(id: AccountId, include: List<TestUseCases.AccountInclude>): HttpDataResponse<JsonApiDocument<AccountResource>> =
        runBlocking {
            val includeQuery = include.joinToString { it.name.lowercase() }.let {
                if (it.isNotBlank()) "?include=$it" else ""
            }
            client.get("/account/${id.value}$includeQuery").toHttpDataResponse<JsonApiDocument<AccountResource>>()
        }

    context(ApplicationTestBuilder)
    fun searchLoanByAccountId(accountId: AccountId): HttpDataResponse<LoanByAccountIdDocument> =
        runBlocking { client.get("/account/${accountId.value}/loan").toHttpDataResponse() }

    context(ApplicationTestBuilder)
    fun createBook(
        isbn: BookIsbn,
        title: BookTitle,
        author: BookAuthor,
        pages: BookPages,
        publisher: BookPublisher,
    ): HttpDataResponse<JsonApiDocument<BookByIdResource>> =
        runBlocking {
            client.post("/book") {
                val request = JsonApiRequestDocument(
                    data = BookRequestResource(
                        attributes = BookRequestResource.Attributes(
                            isbn.value,
                            title.value,
                            author.value,
                            pages.value,
                            publisher.value,
                        ),
                    ),
                )
                setBody(jsonMapper.encodeToString(request))
                contentType(ContentType.JsonApi)
            }.toHttpDataResponse()
        }

    context(ApplicationTestBuilder)
    fun findBookById(id: BookId): HttpDataResponse<JsonApiDocument<BookByIdResource>> =
        runBlocking { client.get("/book/${id.value}").toHttpDataResponse() }

    context(ApplicationTestBuilder)
    fun findBookByIsbn(isbn: BookIsbn): HttpDataResponse<JsonApiListDocument<BookByIsbnResource>> =
        runBlocking { client.get("/book?filter[isbn]=${isbn.value}").toHttpDataResponse() }

    context(ApplicationTestBuilder)
    fun searchBooks(): HttpDataResponse<JsonApiListDocument<BookByIsbnResource>> =
        runBlocking { client.get("/book").toHttpDataResponse() }

    context(ApplicationTestBuilder)
    fun createLoan(
        bookIsbn: BookIsbn,
        accountId: AccountId,
    ): HttpDataResponse<JsonApiDocument<LoanResource>> =
        runBlocking {
            client.post("/loan") {
                val request = JsonApiRequestDocument(
                    LoanRequestResource(
                        attributes = LoanRequestResource.Attributes(
                            bookIsbn = bookIsbn.value,
                            accountId = accountId.value,
                        ),
                    ),
                )
                setBody(jsonMapper.encodeToString(request))
                contentType(ContentType.JsonApi)
            }.toHttpDataResponse()
        }

    context(ApplicationTestBuilder)
    fun finishLoan(bookId: BookId): HttpDataResponse<Unit> =
        runBlocking {
            client.post("/loan/${bookId.value}").toHttpDataResponse()
        }

    private suspend inline fun <reified T> HttpResponse.toHttpDataResponse(): HttpDataResponse<T> {
        val body = bodyAsText()
        return HttpDataResponse(
            value = decodeJsonBody<T?>(body),
            httpResponse = this,
            error = decodeJsonBody<ErrorResponseDocument?>(body),
        )
    }

    private inline fun <reified T> decodeJsonBody(body: String): T? =
        try {
            logger.trace { "Client Response: $body" }
            jsonMapper.decodeFromString<T>(body)
        } catch (e: Exception) {
            null
        }
}
