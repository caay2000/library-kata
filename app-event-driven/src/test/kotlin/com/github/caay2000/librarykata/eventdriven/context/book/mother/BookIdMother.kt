package com.github.caay2000.librarykata.eventdriven.context.book.mother

import com.github.caay2000.librarykata.eventdriven.context.book.domain.BookId
import java.util.UUID

object BookIdMother {
    fun random(): BookId = BookId(UUID.randomUUID())
}
