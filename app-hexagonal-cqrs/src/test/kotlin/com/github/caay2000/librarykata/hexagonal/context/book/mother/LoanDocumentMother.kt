// package com.github.caay2000.librarykata.hexagonal.context.book.mother
//
// import com.github.caay2000.librarykata.hexagonal.context.domain.Book
// import com.github.caay2000.librarykata.hexagonal.context.domain.LoanId
// import com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.serialization.LoanDocument
// import java.time.LocalDateTime
//
// object LoanDocumentMother {
//
//    fun Book.toLoanDocument(loanId: LoanId, startedAt: LocalDateTime, finishedAt: LocalDateTime? = null) =
//        LoanDocument(
//            data = LoanDocument.Resource(
//                id = loanId.value,
//                attributes = LoanDocument.Resource.Attributes(
//                    bookId = id.value,
//                    accountId = ,
//                    startLoan = startedAt,
//                    finishLoan = finishedAt,
//                ),
//            ),
//        )
// }
