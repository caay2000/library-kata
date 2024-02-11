package com.github.caay2000.librarykata.eventdriven.context.loan.primaryadapter.http.transformer

import com.github.caay2000.common.cqrs.QueryHandler
import com.github.caay2000.common.http.Transformer
import com.github.caay2000.common.http.shouldProcess
import com.github.caay2000.common.jsonapi.JsonApiDocument
import com.github.caay2000.common.jsonapi.JsonApiIncludedResource
import com.github.caay2000.common.jsonapi.JsonApiRelationshipData
import com.github.caay2000.common.resourcebus.JsonApiResourceBus
import com.github.caay2000.common.resourcebus.JsonApiResourceBusError
import com.github.caay2000.librarykata.eventdriven.context.loan.application.account.find.FindAccountQuery
import com.github.caay2000.librarykata.eventdriven.context.loan.application.account.find.FindAccountQueryHandler
import com.github.caay2000.librarykata.eventdriven.context.loan.application.account.find.FindAccountQueryResponse
import com.github.caay2000.librarykata.eventdriven.context.loan.application.book.find.FindBookQuery
import com.github.caay2000.librarykata.eventdriven.context.loan.application.book.find.FindBookQueryHandler
import com.github.caay2000.librarykata.eventdriven.context.loan.application.book.find.FindBookQueryResponse
import com.github.caay2000.librarykata.eventdriven.context.loan.domain.Account
import com.github.caay2000.librarykata.eventdriven.context.loan.domain.AccountId
import com.github.caay2000.librarykata.eventdriven.context.loan.domain.AccountRepository
import com.github.caay2000.librarykata.eventdriven.context.loan.domain.Book
import com.github.caay2000.librarykata.eventdriven.context.loan.domain.BookId
import com.github.caay2000.librarykata.eventdriven.context.loan.domain.BookRepository
import com.github.caay2000.librarykata.eventdriven.context.loan.domain.Loan
import com.github.caay2000.librarykata.jsonapi.context.account.AccountResource
import com.github.caay2000.librarykata.jsonapi.context.book.BookResource
import com.github.caay2000.librarykata.jsonapi.context.loan.LoanResource
import com.github.caay2000.librarykata.jsonapi.transformer.RelationshipIdentifier
import com.github.caay2000.librarykata.jsonapi.transformer.RelationshipTransformer

class LoanDocumentTransformer(
    accountRepository: AccountRepository,
    bookRepository: BookRepository,
    private val resourceBus: JsonApiResourceBus,
) : Transformer<Loan, JsonApiDocument<LoanResource>> {
    private val accountQueryHandler: QueryHandler<FindAccountQuery, FindAccountQueryResponse> =
        FindAccountQueryHandler(accountRepository)
    private val bookQueryHandler: QueryHandler<FindBookQuery, FindBookQueryResponse> = FindBookQueryHandler(bookRepository)

    override fun invoke(
        value: Loan,
        include: List<String>,
    ): JsonApiDocument<LoanResource> {
        val account = accountQueryHandler.invoke(FindAccountQuery(value.accountId)).account
        val book = bookQueryHandler.invoke(FindBookQuery(value.bookId)).book
        return value.toJsonApiDocument(account, book, include)
    }

    private fun Loan.toJsonApiDocument(
        account: Account? = null,
        book: Book? = null,
        include: List<String> = emptyList(),
    ): JsonApiDocument<LoanResource> =
        JsonApiDocument(
            data = toJsonApiLoanResource(account, book),
            included = manageLoanIncludes(include, null, null),
        )

    private fun manageLoanIncludes(
        include: List<String>,
        accountId: String?,
        book: Book?,
    ): MutableSet<JsonApiIncludedResource>? {
        if (include.isEmpty()) return null
        val included = mutableSetOf<JsonApiIncludedResource>()
        if (include.shouldProcess(AccountResource.TYPE) && accountId != null) {
            val resource =
                try {
                    resourceBus.retrieve<LoanResource>(accountId)
                } catch (e: JsonApiResourceBusError.ResourceHandlerNotFound) {
                    null
                }
            if (resource != null) {
//                included.addAll(listOf(resource.toJsonApiIncludedResource()))
            }
        }
        if (include.shouldProcess(BookResource.TYPE) && book != null) {
//        val elements = BookIncludeTransformer().invoke(listOf(book))
            val elements = emptyList<JsonApiIncludedResource>()
            if (elements != null) {
                included.addAll(elements)
            }
        }
        return included
    }
}

fun Loan.toJsonApiLoanResource(
    account: Account? = null,
    book: Book? = null,
) = LoanResource(
    id = id.value,
    type = LoanResource.TYPE,
    attributes = toJsonApiDocumentLoanAttributes(),
    relationships = manageLoanRelationships(account?.id, book?.id),
)

private fun manageLoanRelationships(
    accountId: AccountId?,
    bookId: BookId?,
): Map<String, JsonApiRelationshipData> {
    val map = mutableMapOf<String, JsonApiRelationshipData>()
    if (accountId != null) {
        val relationship = RelationshipTransformer.invoke(RelationshipIdentifier(accountId.value, AccountResource.TYPE))
        if (relationship != null) {
            map.putAll(relationship)
        }
    }
    if (bookId != null) {
        val relationship = RelationshipTransformer.invoke(RelationshipIdentifier(bookId.value, BookResource.TYPE))
        if (relationship != null) {
            map.putAll(relationship)
        }
    }
    return map
}

internal fun Loan.toJsonApiDocumentLoanAttributes() =
    LoanResource.Attributes(
        bookId = bookId.value,
        accountId = accountId.value,
        startLoan = createdAt.value,
        finishLoan = finishedAt?.value,
    )
