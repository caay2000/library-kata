package com.github.caay2000.librarykata.eventdriven.context.book.primaryadapter.http.transformer

import com.github.caay2000.common.http.Transformer
import com.github.caay2000.common.http.shouldProcess
import com.github.caay2000.common.jsonapi.JsonApiDocument
import com.github.caay2000.librarykata.eventdriven.context.book.domain.Book
import com.github.caay2000.librarykata.eventdriven.context.loan.loan.domain.Loan
import com.github.caay2000.librarykata.eventdriven.context.loan.loan.primaryadapter.http.serialization.LoanIncludeTransformer
import com.github.caay2000.librarykata.eventdriven.context.loan.loan.primaryadapter.http.serialization.LoanRelationshipTransformer
import com.github.caay2000.librarykata.jsonapi.context.book.BookResource
import com.github.caay2000.librarykata.jsonapi.context.loan.LoanResource

class BookDocumentTransformer() : Transformer<Book, JsonApiDocument<BookResource>> {
//    private val loanQueryHandler: QueryHandler<SearchLoanQuery, SearchLoanQueryResponse> = SearchLoanQueryHandler(loanRepository)

    override fun invoke(
        value: Book,
        include: List<String>,
    ): JsonApiDocument<BookResource> {
        // TODO When the Book is brand new, this query is not needed, as it won't have any relationship
//        val loans = loanQueryHandler.invoke(SearchLoanQuery.SearchLoanByBookIdQuery(value.id.value)).value
        return value.toJsonApiBookDocument(emptyList(), include)
    }
}

fun Book.toJsonApiBookDocument(
    loans: List<Loan> = emptyList(),
    include: List<String> = emptyList(),
) = JsonApiDocument(
    data = toJsonApiBookResource(loans),
    included = if (include.shouldProcess(LoanResource.TYPE)) LoanIncludeTransformer().invoke(loans) else null,
)

internal fun Book.toJsonApiBookResource(loans: List<Loan> = emptyList()) =
    BookResource(
        id = id.value,
        type = BookResource.TYPE,
        attributes = toJsonApiBookAttributes(),
        relationships = LoanRelationshipTransformer().invoke(loans),
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
