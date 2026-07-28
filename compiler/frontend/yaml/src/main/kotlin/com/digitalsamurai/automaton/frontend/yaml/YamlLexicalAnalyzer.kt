package com.digitalsamurai.automaton.frontend.yaml

import com.digitalsamurai.automaton.frontend.api.AutomatonLexicalAnalyzer
import com.digitalsamurai.automaton.grammar.AutomatonGrammar
import com.digitalsamurai.automaton.grammar.GrammarSymbol
import com.digitalsamurai.automaton.grammar.Terminal
import com.digitalsamurai.automaton.grammar.Token
import org.yaml.snakeyaml.LoaderOptions
import org.yaml.snakeyaml.events.ScalarEvent
import org.yaml.snakeyaml.parser.ParserImpl
import org.yaml.snakeyaml.reader.StreamReader
import org.yaml.snakeyaml.scanner.ScannerImpl
import java.io.InputStream

public class YamlLexicalAnalyzer: AutomatonLexicalAnalyzer<InputStream> {

    override fun analyze(input: InputStream): Sequence<Token<*>> {
        val parser = ParserImpl(ScannerImpl(StreamReader(input.reader()), LoaderOptions()))

        var requiredTerminal: Terminal<*>? = null
        val output = mutableListOf<Token<*>>()
        while (parser.peekEvent() != null) {
            val event = parser.event

            when(event) {
                is ScalarEvent -> {
                    // check required data for terminal
                    if (requiredTerminal != null) {
                        val token = requiredTerminal.asToken(
                            sourceLocation = "yaml",
                            value = event.value
                        )
                        output.add(token)
                        requiredTerminal = null
                        continue
                    }

                    // check if it is terminal
                    val terminal = AutomatonGrammar.terminals[event.value]
                    if (terminal != null) {
                        // when terminal was found, at the next step we wait data for terminal
                        requiredTerminal = terminal
                        continue
                    }

                    val nonTerminal = AutomatonGrammar.nonTerminals[event.value]
                    if (nonTerminal != null) {
                        val token = Token(
                            symbol = nonTerminal,
                            value = Unit,
                            sourceLocation = "yaml",
                        )
                        output.add(token)
                        continue
                    }

                    error("Illegal symbol: ${event.value}")
                }
                else -> {
                    // skipped
                }
            }
        }
        return output.asSequence()
    }
}