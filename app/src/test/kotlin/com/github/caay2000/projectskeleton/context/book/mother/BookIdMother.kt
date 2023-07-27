package com.github.caay2000.projectskeleton.context.book.mother

import com.github.caay2000.projectskeleton.context.book.domain.BookId
import java.util.UUID

object BookIdMother {
    fun random(): BookId = BookId(UUID.randomUUID())
}
