package com.github.caay2000.projectskeleton.context.communication.secondaryadapter.database

import arrow.core.Either
import com.github.caay2000.projectskeleton.context.communication.application.TemplateRepository
import com.github.caay2000.projectskeleton.context.communication.domain.Body
import com.github.caay2000.projectskeleton.context.communication.domain.Subject
import com.github.caay2000.projectskeleton.context.communication.domain.Template
import com.github.caay2000.projectskeleton.context.communication.domain.TemplateId

class InMemoryTemplateRepository : TemplateRepository {

    private val templates: Set<Template> = setOf(
        Template(TemplateId.USER_CREATED_TEMPLATE, Subject("Welcome email"), Body("Welcome to our platform!")),
    )

    override fun findTemplateById(id: TemplateId): Either<Throwable, Template> =
        Either.catch { templates.first { it.id == id } }
}
