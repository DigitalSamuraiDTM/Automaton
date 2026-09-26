package com.digitalsamurai.automaton.parser.ll1

import com.digitalsamurai.automaton.ast.Ast
import com.digitalsamurai.automaton.grammar.Camera
import com.digitalsamurai.automaton.grammar.Duration
import com.digitalsamurai.automaton.grammar.Element
import com.digitalsamurai.automaton.grammar.ElementProperty
import com.digitalsamurai.automaton.grammar.Id
import com.digitalsamurai.automaton.grammar.InputData
import com.digitalsamurai.automaton.grammar.Microphone
import com.digitalsamurai.automaton.grammar.PropertyValue
import com.digitalsamurai.automaton.grammar.TapType
import com.digitalsamurai.automaton.grammar.Terminal
import com.digitalsamurai.automaton.grammar.TestName
import com.digitalsamurai.automaton.grammar.ThemeMode
import com.digitalsamurai.automaton.grammar.Timeout
import com.digitalsamurai.automaton.grammar.Token

class AstLeafParser {

    fun parse(token: Token<*>): Ast.Leaf<*> {
        return when(token.symbol as Terminal<*>) {
            Camera -> Ast.Leaf.AstCameraLeaf(token.value as Boolean)
            Duration -> Ast.Leaf.AstDuration(token.value as kotlin.time.Duration)
            Element.Layout -> Ast.Leaf.AstElementLayoutLeaf(token.value as String)
            Element.Name -> Ast.Leaf.AstElementNameLeaf(token.value as String)
            ElementProperty -> Ast.Leaf.AstElementProperty(token.value as String)
            Id -> Ast.Leaf.AstId(token.value as String)
            InputData -> Ast.Leaf.AstInputData(token.value as String)
            Microphone -> Ast.Leaf.AstMicrophoneLeaf(token.value as Boolean)
            PropertyValue -> Ast.Leaf.AstPropertyValue(token.value as String)
            TapType -> Ast.Leaf.AstTapType((token.value as TapType.Type).toType())
            TestName -> Ast.Leaf.AstTestName(token.value as String)
            ThemeMode -> Ast.Leaf.AstThemeMode((token.value as ThemeMode.Mode).toMode())
            Timeout -> Ast.Leaf.AstTimeout(token.value as kotlin.time.Duration)
        }
    }

    private fun TapType.Type.toType(): Ast.Leaf.AstTapType.Type {
        return when (this) {
            TapType.Type.SINGLE -> Ast.Leaf.AstTapType.Type.SINGLE
        }
    }

    private fun ThemeMode.Mode.toMode(): Ast.Leaf.AstThemeMode.Mode {
        return when (this) {
            ThemeMode.Mode.LIGHT -> Ast.Leaf.AstThemeMode.Mode.LIGHT
            ThemeMode.Mode.DARK -> Ast.Leaf.AstThemeMode.Mode.DARK
            ThemeMode.Mode.SYSTEM -> Ast.Leaf.AstThemeMode.Mode.SYSTEM
        }
    }

}