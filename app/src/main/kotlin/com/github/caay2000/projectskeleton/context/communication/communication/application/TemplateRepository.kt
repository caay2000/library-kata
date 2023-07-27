package com.github.caay2000.projectskeleton.context.communication.communication.application

import arrow.core.Either
import com.github.caay2000.common.database.Repository
import com.github.caay2000.common.database.RepositoryError
import com.github.caay2000.projectskeleton.context.communication.communication.domain.Template
import com.github.caay2000.projectskeleton.context.communication.communication.domain.TemplateId

interface TemplateRepository : Repository {

    fun findTemplateById(id: TemplateId): Either<RepositoryError, Template>
}
