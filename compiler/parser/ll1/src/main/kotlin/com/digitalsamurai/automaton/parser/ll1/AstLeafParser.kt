package com.digitalsamurai.automaton.parser.ll1

import com.digitalsamurai.automaton.ast.Ast
import com.digitalsamurai.automaton.ast.Ast.Leaf.*
import com.digitalsamurai.automaton.grammar.*
import org.json.JSONObject
import java.net.URL

class AstLeafParser {

    fun parse(token: Token<*>): Ast.Leaf<*> {
        return when (token.symbol as Terminal<*>) {
            Camera -> AstCameraLeaf(token.value as Boolean)
            Duration -> AstDuration(token.value as kotlin.time.Duration)
            Element.Layout -> AstElementLayoutLeaf(token.value as String)
            Element.Name -> AstElementNameLeaf(token.value as String)
            ElementProperty -> AstElementProperty(token.value as String)
            Id -> AstId(token.value as String)
            InputData -> AstInputData(token.value as String)
            Microphone -> AstMicrophoneLeaf(token.value as Boolean)
            PropertyValue -> AstPropertyValue(token.value as String)
            TapType -> AstTapType((token.value as TapType.Type).toType())
            TestName -> AstTestName(token.value as String)
            ThemeMode -> AstThemeMode((token.value as ThemeMode.Mode).toMode())
            Timeout -> AstTimeout(token.value as kotlin.time.Duration)
            Step.Description -> AstStepDescription(token.value as String)
            HttpRequest.Body.Json -> AstHttpRequestJsonBody((token.value as HttpRequest.Body.Json.Type).toType())
            HttpRequest.Body.Plain -> AstHttpRequestPlainBody(token.value as String)
            Request.Url -> AstHttpRequestUrl(token.value as URL)
            Request.Name -> AstHttpRequestName(token.value as String)
        }
    }

    private fun HttpRequest.Body.Json.Type.toType(): AstHttpRequestJsonBody.Type {
        return when (this) {
            is HttpRequest.Body.Json.Type.Array -> AstHttpRequestJsonBody.Type.Array(this)
            is HttpRequest.Body.Json.Type.Object -> AstHttpRequestJsonBody.Type.Object(this)
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