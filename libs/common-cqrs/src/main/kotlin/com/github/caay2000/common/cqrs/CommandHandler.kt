package com.github.caay2000.projectskeleton.common.cqrs

interface CommandHandler<T : Command> {

    fun invoke(command: T)
}

interface Command
