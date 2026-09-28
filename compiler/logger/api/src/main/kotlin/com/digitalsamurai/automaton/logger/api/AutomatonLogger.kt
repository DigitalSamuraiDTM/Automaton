package com.digitalsamurai.automaton.logger.api

import com.digitalsamurai.automaton.api.Automaton

public fun Automaton.loggers(): AutomatonLoggers {
    return AutomatonLoggers
}

public object AutomatonLoggers {

}

public abstract class AutomatonLogger(
    public open val enabledLevels: Set<LogLevel>
) {

    /**
     * synced abstract fun should be realized with fast log function without blocking or heavy tasks
     */
    abstract fun log(level: LogLevel, message: String)

    fun info(message: String) = log(LogLevel.INFO, message)

    fun debug(message: String) = log(LogLevel.DEBUG, message)

    fun warning(message: String) = log(LogLevel.WARN, message)

    fun error(exception: Exception) = log(LogLevel.ERROR, "Exception: $exception")

    enum class LogLevel {
        DEBUG,
        INFO,
        WARN,
        ERROR,
    }
}