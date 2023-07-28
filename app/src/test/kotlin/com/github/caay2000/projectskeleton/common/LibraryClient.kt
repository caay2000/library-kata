package com.github.caay2000.projectskeleton.common

import com.github.caay2000.common.http.ErrorResponseDocument
import com.github.caay2000.common.test.http.HttpDataResponse
import com.github.caay2000.projectskeleton.context.account.domain.AccountId
import com.github.caay2000.projectskeleton.context.account.domain.Birthdate
import com.github.caay2000.projectskeleton.context.account.domain.Email
import com.github.caay2000.projectskeleton.context.account.domain.IdentityNumber
import com.github.caay2000.projectskeleton.context.account.domain.Name
import com.github.caay2000.projectskeleton.context.account.domain.PhoneNumber
import com.github.caay2000.projectskeleton.context.account.domain.PhonePrefix
import com.github.caay2000.projectskeleton.context.account.domain.Surname
import com.github.caay2000.projectskeleton.context.account.primaryadapter.http.serialization.AccountDetailsDocument
import com.github.caay2000.projectskeleton.context.account.primaryadapter.http.serialization.CreateAccountRequestDocument
import com.github.caay2000.projectskeleton.context.book.domain.BookAuthor
import com.github.caay2000.projectskeleton.context.book.domain.BookId
import com.github.caay2000.projectskeleton.context.book.domain.BookIsbn
import com.github.caay2000.projectskeleton.context.book.domain.BookPages
import com.github.caay2000.projectskeleton.context.book.domain.BookPublisher
import com.github.caay2000.projectskeleton.context.book.domain.BookTitle
import com.github.caay2000.projectskeleton.context.book.primaryadapter.http.serialization.BookByIdDocument
import com.github.caay2000.projectskeleton.context.book.primaryadapter.http.serialization.BookCreateRequestDocument
import com.github.caay2000.projectskeleton.context.book.primaryadapter.http.serialization.BookDocument
import com.github.caay2000.projectskeleton.context.loan.domain.UserId
import com.github.caay2000.projectskeleton.context.loan.primaryadapter.http.serialization.LoanDocument
import com.github.caay2000.projectskeleton.context.loan.primaryadapter.http.serialization.LoanRequestDocument
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.server.testing.ApplicationTestBuilder
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import com.github.caay2000.projectskeleton.context.loan.domain.BookId as LoanBookId

class LibraryClient {

    context(ApplicationTestBuilder)
    fun createAccount(
        identityNumber: IdentityNumber,
        name: Name,
        surname: Surname,
        birthdate: Birthdate,
        email: Email,
        phonePrefix: PhonePrefix,
        phoneNumber: PhoneNumber,
    ): HttpDataResponse<AccountDetailsDocument> =
        runBlocking {
            client.post("/account") {
                setBody(
                    Json.encodeToString(
                        CreateAccountRequestDocument(
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
                contentType(ContentType.Application.Json)
            }.toHttpDataResponse()
        }

    context(ApplicationTestBuilder)
    fun findAccount(id: AccountId): HttpDataResponse<AccountDetailsDocument> =
        runBlocking { client.get("/account/${id.value}").toHttpDataResponse() }

    context(ApplicationTestBuilder)
    fun createBook(
        isbn: BookIsbn,
        title: BookTitle,
        author: BookAuthor,
        pages: BookPages,
        publisher: BookPublisher,
    ): HttpDataResponse<BookByIdDocument> =
        runBlocking {
            client.post("/book") {
                val request = BookCreateRequestDocument(isbn.value, title.value, author.value, pages.value, publisher.value)
                setBody(Json.encodeToString(request))
                contentType(ContentType.Application.Json)
            }.toHttpDataResponse()
        }

    context(ApplicationTestBuilder)
    fun findBookById(id: BookId): HttpDataResponse<BookByIdDocument> =
        runBlocking { client.get("/book/${id.value}").toHttpDataResponse() }

    context(ApplicationTestBuilder)
    fun findBookByIsbn(isbn: BookIsbn): HttpDataResponse<BookDocument> =
        runBlocking { client.get("/book?isbn=${isbn.value}").toHttpDataResponse() }

    context(ApplicationTestBuilder)
    fun createLoan(
        bookId: LoanBookId,
        userId: UserId,
    ): HttpDataResponse<LoanDocument> =
        runBlocking {
            client.post("/loan") {
                val request = LoanRequestDocument(bookId = bookId.value, userId = userId.value)
                setBody(Json.encodeToString(request))
                contentType(ContentType.Application.Json)
            }.toHttpDataResponse()
        }

    private suspend inline fun <reified T> HttpResponse.toHttpDataResponse(): HttpDataResponse<T> {
        val body = bodyAsText()

        return HttpDataResponse(
            value = decodeJsonBody<T>(body),
            httpResponse = this,
            error = decodeJsonBody<ErrorResponseDocument>(body),
        )
    }

    private inline fun <reified T> decodeJsonBody(body: String): T? =
        try {
            Json.decodeFromJsonElement<T>(Json.parseToJsonElement(body))
        } catch (e: Exception) {
            null
        }
}
