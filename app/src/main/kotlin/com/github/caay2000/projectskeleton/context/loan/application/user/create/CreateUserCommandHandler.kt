package com.github.caay2000.projectskeleton.context.loan.application.user.create

import com.github.caay2000.common.arrow.getOrThrow
import com.github.caay2000.projectskeleton.common.cqrs.Command
import com.github.caay2000.projectskeleton.common.cqrs.CommandHandler
import com.github.caay2000.projectskeleton.context.loan.application.UserRepository
import com.github.caay2000.projectskeleton.context.loan.domain.UserId
import java.util.UUID

class CreateUserCommandHandler(userRepository: UserRepository) : CommandHandler<CreateUserCommand> {

    private val creator = UserCreator(userRepository)

    override fun invoke(command: CreateUserCommand): Unit =
        creator.invoke(userId = UserId(command.userId)).getOrThrow()
}

data class CreateUserCommand(val userId: UUID) : Command
