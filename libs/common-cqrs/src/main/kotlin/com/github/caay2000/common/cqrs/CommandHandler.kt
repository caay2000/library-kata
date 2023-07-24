package com.github.caay2000.archkata.common.cqrs

interface CommandHandler<T : Command> {

    fun invoke(command: T)
}

interface Command
