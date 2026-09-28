package com.digitalsamurai.automaton.logger.terminal

import com.digitalsamurai.automaton.logger.api.AutomatonLogger
import java.text.SimpleDateFormat
import java.util.Date

public class TerminalLogger(
    override val enabledLevels: Set<LogLevel>,
): AutomatonLogger(enabledLevels) {

    val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")

    override fun log(level: LogLevel, message: String) {
        println("${sdf.format(Date())} ${level.name}: $message")
    }
}