package com.github.caay2000.librarykata.context.loan.application.user.create

import com.github.caay2000.common.arrow.getOrThrow
import com.github.caay2000.common.cqrs.Command
import com.github.caay2000.common.cqrs.CommandHandler
import com.github.caay2000.librarykata.context.loan.application.UserRepository
import com.github.caay2000.librarykata.context.loan.domain.UserId
import mu.KLogger
import mu.KotlinLogging
import java.util.UUID

class CreateUserCommandHandler(userRepository: UserRepository) : CommandHandler<CreateUserCommand> {

    override val logger: KLogger = KotlinLogging.logger {}
    private val creator = UserCreator(userRepository)

    override fun handle(command: CreateUserCommand): Unit =
        creator.invoke(userId = UserId(command.userId)).getOrThrow()
}

data class CreateUserCommand(val userId: UUID) : Command
