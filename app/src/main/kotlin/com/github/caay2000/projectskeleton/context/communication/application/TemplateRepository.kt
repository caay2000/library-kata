package com.github.caay2000.projectskeleton.context.communication.application

import arrow.core.Either
import com.github.caay2000.projectskeleton.context.communication.domain.Template
import com.github.caay2000.projectskeleton.context.communication.domain.TemplateId

interface TemplateRepository {

    fun findTemplateById(id: TemplateId): Either<Throwable, Template>
}
