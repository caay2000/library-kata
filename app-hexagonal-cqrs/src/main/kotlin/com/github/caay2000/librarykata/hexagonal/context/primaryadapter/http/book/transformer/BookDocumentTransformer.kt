package com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.book.transformer

import com.github.caay2000.common.cqrs.QueryHandler
import com.github.caay2000.common.http.Transformer
import com.github.caay2000.common.http.shouldProcess
import com.github.caay2000.common.jsonapi.JsonApiDocument
import com.github.caay2000.librarykata.hexagonal.context.application.loan.search.SearchLoanQuery
import com.github.caay2000.librarykata.hexagonal.context.application.loan.search.SearchLoanQueryHandler
import com.github.caay2000.librarykata.hexagonal.context.application.loan.search.SearchLoanQueryResponse
import com.github.caay2000.librarykata.hexagonal.context.domain.book.Book
import com.github.caay2000.librarykata.hexagonal.context.domain.loan.Loan
import com.github.caay2000.librarykata.hexagonal.context.domain.loan.LoanRepository
import com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.loan.serialization.LoanIncludeTransformer
import com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.loan.serialization.LoanRelationshipTransformer
import com.github.caay2000.librarykata.jsonapi.context.book.BookResource
import com.github.caay2000.librarykata.jsonapi.context.loan.LoanResource

class BookDocumentTransformer(loanRepository: LoanRepository) : Transformer<Book, JsonApiDocument<BookResource>> {
    private val loanQueryHandler: QueryHandler<SearchLoanQuery, SearchLoanQueryResponse> = SearchLoanQueryHandler(loanRepository)

    override fun invoke(
        value: Book,
        include: List<String>,
    ): JsonApiDocument<BookResource> {
        // TODO When the Book is brand new, this query is not needed, as it won't have any relationship
        val loans = loanQueryHandler.invoke(SearchLoanQuery.SearchLoanByBookIdQuery(value.id.value)).value
        return value.toJsonApiDocument(loans, include)
    }
}

fun Book.toJsonApiDocument(
    loans: List<Loan> = emptyList(),
    include: List<String> = emptyList(),
) = JsonApiDocument(
    data = toJsonApiDocumentBookResource(loans),
    included = if (include.shouldProcess(LoanResource.TYPE)) LoanIncludeTransformer().invoke(loans) else null,
)

internal fun Book.toJsonApiDocumentBookResource(loans: List<Loan> = emptyList()) =
    BookResource(
        id = id.value,
        type = "book",
        attributes = toJsonApiDocumentBookAttributes(),
        relationships = LoanRelationshipTransformer().invoke(loans),
    )

private fun Book.toJsonApiDocumentBookAttributes() =
    BookResource.Attributes(
        isbn = isbn.value,
        title = title.value,
        author = author.value,
        pages = pages.value,
        publisher = publisher.value,
        available = available.value,
    )
