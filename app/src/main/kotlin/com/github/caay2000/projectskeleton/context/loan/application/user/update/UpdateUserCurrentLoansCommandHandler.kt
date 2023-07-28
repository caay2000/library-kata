package com.github.caay2000.projectskeleton.context.loan.application.user.update

import com.github.caay2000.common.arrow.getOrThrow
import com.github.caay2000.projectskeleton.common.cqrs.Command
import com.github.caay2000.projectskeleton.common.cqrs.CommandHandler
import com.github.caay2000.projectskeleton.context.loan.application.UserRepository
import com.github.caay2000.projectskeleton.context.loan.domain.UserId
import java.util.UUID

class UpdateUserCurrentLoansCommandHandler(userRepository: UserRepository) : CommandHandler<UpdateUserCurrentLoansCommand> {

    private val updater = UserCurrentLoansUpdater(userRepository)

    override fun invoke(command: UpdateUserCurrentLoansCommand): Unit =
        updater.invoke(userId = UserId(command.userId), command.value).getOrThrow()
}

data class UpdateUserCurrentLoansCommand(
    val userId: UUID,
    val value: Int,
) : Command
