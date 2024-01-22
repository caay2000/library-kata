package com.github.caay2000.common.cqrs

import mu.KLogger

interface CommandHandler<T : Command> {
    val logger: KLogger

    fun invoke(command: T) {
        logger.info { ">> processing $command" }
        return handle(command).also {
            logger.info { "<< processed $command" }
        }
    }

    fun handle(command: T)
}

interface Command
