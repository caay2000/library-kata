package com.github.caay2000.projectskeleton.context.communication.communication.domain

sealed class Template(
    open val id: TemplateId,
    val channel: TemplateChannel,
)

data class EmailTemplate(
    override val id: TemplateId,
    val subject: Subject,
    val body: Body,
) : Template(id, TemplateChannel.EMAIL)

data class SMSTemplate(
    override val id: TemplateId,
    val message: Message,
) : Template(id, TemplateChannel.SMS)

enum class TemplateId {
    USER_CREATED,
    USER_UPDATED,
}

@JvmInline
value class Subject(val value: String)

@JvmInline
value class Body(val value: String)

@JvmInline
value class Message(val value: String)

enum class TemplateChannel {
    UNDEFINED, SMS, EMAIL
}
