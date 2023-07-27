package com.github.caay2000.projectskeleton.context.communication.communication.secondaryadapter.database

import arrow.core.Either
import com.github.caay2000.common.database.RepositoryError
import com.github.caay2000.projectskeleton.context.communication.communication.application.TemplateRepository
import com.github.caay2000.projectskeleton.context.communication.communication.domain.Body
import com.github.caay2000.projectskeleton.context.communication.communication.domain.EmailTemplate
import com.github.caay2000.projectskeleton.context.communication.communication.domain.Message
import com.github.caay2000.projectskeleton.context.communication.communication.domain.SMSTemplate
import com.github.caay2000.projectskeleton.context.communication.communication.domain.Subject
import com.github.caay2000.projectskeleton.context.communication.communication.domain.Template
import com.github.caay2000.projectskeleton.context.communication.communication.domain.TemplateId

class InMemoryTemplateRepository : TemplateRepository {

    private val templates: Set<Template> = setOf(
        EmailTemplate(
            id = TemplateId.USER_CREATED,
            subject = Subject("Welcome email"),
            body = Body("Welcome to our platform!"),
        ),
        SMSTemplate(
            id = TemplateId.USER_UPDATED,
            message = Message("Your Contact Details has been updated"),
        ),
    )

    override fun findTemplateById(id: TemplateId): Either<RepositoryError, Template> =
        Either.catch { templates.first { it.id == id } }
            .mapLeft {
                when (it) {
                    is NoSuchElementException -> RepositoryError.NotFoundError()
                    else -> RepositoryError.Unknown(it)
                }
            }
}
