package com.github.caay2000.projectskeleton.context.communication.domain

data class Template(
    val id: TemplateId,
    val subject: Subject,
    val body: Body,
)

enum class TemplateId {
    USER_CREATED_TEMPLATE,
}

@JvmInline
value class Subject(val value: String)

@JvmInline
value class Body(val value: String)
