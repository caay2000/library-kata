package com.github.caay2000.librarykata.hexagonal.context.book.primaryadapter.http.transformer

import com.github.caay2000.common.cqrs.QueryHandler
import com.github.caay2000.common.http.Transformer
import com.github.caay2000.common.http.shouldProcess
import com.github.caay2000.common.jsonapi.JsonApiDocument
import com.github.caay2000.common.jsonapi.JsonApiIncludedResource
import com.github.caay2000.librarykata.hexagonal.context.account.application.find.FindAccountQuery
import com.github.caay2000.librarykata.hexagonal.context.account.application.find.FindAccountQueryHandler
import com.github.caay2000.librarykata.hexagonal.context.account.application.find.FindAccountQueryResponse
import com.github.caay2000.librarykata.hexagonal.context.account.domain.Account
import com.github.caay2000.librarykata.hexagonal.context.account.domain.AccountRepository
import com.github.caay2000.librarykata.hexagonal.context.account.primaryadapter.http.transformer.toJsonApiAccountResource
import com.github.caay2000.librarykata.hexagonal.context.book.domain.Book
import com.github.caay2000.librarykata.hexagonal.context.loan.application.search.SearchLoanQuery
import com.github.caay2000.librarykata.hexagonal.context.loan.application.search.SearchLoanQueryHandler
import com.github.caay2000.librarykata.hexagonal.context.loan.application.search.SearchLoanQueryResponse
import com.github.caay2000.librarykata.hexagonal.context.loan.domain.Loan
import com.github.caay2000.librarykata.hexagonal.context.loan.domain.LoanRepository
import com.github.caay2000.librarykata.hexagonal.context.loan.primaryadapter.http.transformer.toJsonApiLoanResource
import com.github.caay2000.librarykata.jsonapi.context.account.AccountResource
import com.github.caay2000.librarykata.jsonapi.context.book.BookResource
import com.github.caay2000.librarykata.jsonapi.context.loan.LoanResource
import com.github.caay2000.librarykata.jsonapi.transformer.IncludeTransformer
import com.github.caay2000.librarykata.jsonapi.transformer.RelationshipIdentifier
import com.github.caay2000.librarykata.jsonapi.transformer.RelationshipTransformer

class BookDocumentTransformer(accountRepository: AccountRepository, loanRepository: LoanRepository) : Transformer<Book, JsonApiDocument<BookResource>> {
    private val accountQueryHandler: QueryHandler<FindAccountQuery, FindAccountQueryResponse> = FindAccountQueryHandler(accountRepository)
    private val loanQueryHandler: QueryHandler<SearchLoanQuery, SearchLoanQueryResponse> = SearchLoanQueryHandler(loanRepository)

    override fun invoke(
        value: Book,
        include: List<String>,
    ): JsonApiDocument<BookResource> {
        // TODO When the Book is brand new, this query is not needed, as it won't have any relationship
        val loans = loanQueryHandler.invoke(SearchLoanQuery.SearchLoanByBookIdQuery(value.id.value)).value
        val accounts =
            loans.map {
                accountQueryHandler.invoke(FindAccountQuery(it.accountId)).account
            }
        return value.toJsonApiBookDocument(accounts, loans, include)
    }
}

fun Book.toJsonApiBookDocument(
    accounts: List<Account> = emptyList(),
    loans: List<Loan> = emptyList(),
    include: List<String> = emptyList(),
) = JsonApiDocument(
    data = toJsonApiBookResource(accounts, loans),
    included = manageBookIncludes(include, accounts, loans),
)

internal fun Book.manageBookIncludes(
    include: List<String>,
    accounts: List<Account>,
    loans: List<Loan>,
): MutableSet<JsonApiIncludedResource>? {
    if (include.isEmpty()) return null
    val included = mutableSetOf<JsonApiIncludedResource>()
    if (include.shouldProcess(AccountResource.TYPE)) {
        val elements = IncludeTransformer.invoke(accounts.map { it.toJsonApiAccountResource(loans.filter { loan -> loan.bookId == this.id }) })
        if (elements != null) included.addAll(elements)
    }
    if (include.shouldProcess(LoanResource.TYPE)) {
        val elements = IncludeTransformer.invoke(loans.map { it.toJsonApiLoanResource() })
        if (elements != null) included.addAll(elements)
    }
    return included
}

internal fun Book.toJsonApiBookResource(
    accounts: List<Account> = emptyList(),
    loans: List<Loan> = emptyList(),
) = BookResource(
    id = id.value,
    type = BookResource.TYPE,
    attributes = toJsonApiBookAttributes(),
    relationships =
        RelationshipTransformer.invoke(
            accounts.map { RelationshipIdentifier(it.id.value, AccountResource.TYPE) } +
                loans.map { RelationshipIdentifier(it.id.value, LoanResource.TYPE) },
        ),
)

internal fun Book.toJsonApiBookAttributes() =
    BookResource.Attributes(
        isbn = isbn.value,
        title = title.value,
        author = author.value,
        pages = pages.value,
        publisher = publisher.value,
        available = available.value,
    )
