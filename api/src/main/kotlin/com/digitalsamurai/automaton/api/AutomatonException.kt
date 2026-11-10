package com.digitalsamurai.automaton.api


// abstract class that used for mark all exceptions to Automaton at every stage
abstract class AutomatonException(override val cause: Throwable? = null): Exception(cause)