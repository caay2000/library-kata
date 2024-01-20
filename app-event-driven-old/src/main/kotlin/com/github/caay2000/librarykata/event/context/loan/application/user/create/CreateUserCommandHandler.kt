package com.github.caay2000.librarykata.event.context.loan.application.user.create

import com.github.caay2000.common.cqrs.Command
import com.github.caay2000.common.cqrs.CommandHandler
import com.github.caay2000.librarykata.event.context.loan.application.UserRepository
import com.github.caay2000.librarykata.event.context.loan.domain.UserId
import mu.KLogger
import mu.KotlinLogging
import java.util.UUID

class CreateUserCommandHandler(userRepository: UserRepository) : CommandHandler<CreateUserCommand> {
    override val logger: KLogger = KotlinLogging.logger {}
    private val creator = UserCreator(userRepository)

    override fun handle(command: CreateUserCommand): Unit = creator.invoke(userId = UserId(command.userId))
}

data class CreateUserCommand(val userId: UUID) : Command