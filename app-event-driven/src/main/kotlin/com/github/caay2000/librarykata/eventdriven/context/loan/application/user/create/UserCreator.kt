package com.github.caay2000.librarykata.eventdriven.context.loan.application.user.create

import com.github.caay2000.librarykata.eventdriven.context.loan.application.UserRepository
import com.github.caay2000.librarykata.eventdriven.context.loan.domain.User
import com.github.caay2000.librarykata.eventdriven.context.loan.domain.UserId

class UserCreator(private val userRepository: UserRepository) {
    fun invoke(userId: UserId) {
        createUser(userId).save()
    }

    private fun createUser(userId: UserId): User = User.create(userId)

    private fun User.save(): User = userRepository.save(this)
}
