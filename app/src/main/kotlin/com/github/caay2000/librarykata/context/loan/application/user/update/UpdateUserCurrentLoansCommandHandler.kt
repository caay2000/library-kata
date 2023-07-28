package com.github.caay2000.librarykata.context.loan.application.user.update

import com.github.caay2000.common.arrow.getOrThrow
import com.github.caay2000.common.cqrs.Command
import com.github.caay2000.common.cqrs.CommandHandler
import com.github.caay2000.librarykata.context.loan.application.UserRepository
import com.github.caay2000.librarykata.context.loan.domain.UserId
import mu.KLogger
import mu.KotlinLogging
import java.util.UUID

class UpdateUserCurrentLoansCommandHandler(userRepository: UserRepository) : CommandHandler<UpdateUserCurrentLoansCommand> {

    override val logger: KLogger = KotlinLogging.logger {}
    private val updater = UserCurrentLoansUpdater(userRepository)

    override fun handle(command: UpdateUserCurrentLoansCommand): Unit =
        updater.invoke(userId = UserId(command.userId), command.value).getOrThrow()
}

data class UpdateUserCurrentLoansCommand(
    val userId: UUID,
    val value: Int,
) : Command
