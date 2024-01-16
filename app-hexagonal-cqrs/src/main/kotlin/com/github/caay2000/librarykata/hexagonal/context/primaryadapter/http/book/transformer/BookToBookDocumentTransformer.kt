package com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.book.transformer

import com.github.caay2000.common.cqrs.QueryHandler
import com.github.caay2000.common.http.Transformer
import com.github.caay2000.common.jsonapi.JsonApiDocument
import com.github.caay2000.librarykata.hexagonal.context.application.loan.search.SearchLoanQuery
import com.github.caay2000.librarykata.hexagonal.context.application.loan.search.SearchLoanQueryHandler
import com.github.caay2000.librarykata.hexagonal.context.application.loan.search.SearchLoanQueryResponse
import com.github.caay2000.librarykata.hexagonal.context.domain.book.Book
import com.github.caay2000.librarykata.hexagonal.context.domain.loan.LoanRepository
import com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.book.FindBookController
import com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.book.serializer.toJsonApiDocumentBookResource
import com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.serialization.toJsonApiDocumentIncludedResource
import com.github.caay2000.librarykata.jsonapi.context.book.BookResource

class BookToBookDocumentTransformer(loanRepository: LoanRepository) : Transformer<Book, JsonApiDocument<BookResource>> {
    private val loanQueryHandler: QueryHandler<SearchLoanQuery, SearchLoanQueryResponse> = SearchLoanQueryHandler(loanRepository)

    override fun invoke(
        book: Book,
        includes: List<String>,
    ): JsonApiDocument<BookResource> {
        // TODO When the Book is brand new, this query is not needed, as it won't have any relationship
        val loans = loanQueryHandler.invoke(SearchLoanQuery.SearchLoanByBookIdQuery(book.id.value)).value
        return JsonApiDocument(
            data = book.toJsonApiDocumentBookResource(loans),
            included =
                includes.shouldProcess(FindBookController.Included.LOANS) {
                    loans.toJsonApiDocumentIncludedResource()
                },
        )
    }
}
