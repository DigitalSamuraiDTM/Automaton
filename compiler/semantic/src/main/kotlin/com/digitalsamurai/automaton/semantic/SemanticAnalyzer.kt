package com.digitalsamurai.automaton.semantic

import com.digitalsamurai.automaton.ast.AstNode
import com.digitalsamurai.automaton.grammar.Element
import com.digitalsamurai.automaton.grammar.Elements

public class SemanticAnalyzer {


    suspend fun analyzeFull(astNode: AstNode<*>): SemanticModel {
        val factory = SemanticModelFactory()
        val model = astNode.buildRecursively(factory)
        TODO("вернуться после фиксов AstNode")
    }


    private fun AstNode<*>.buildRecursively(factory: SemanticModelFactory): SemanticModel.Context {

        error("Unknown ASTNode type: ${this.symbol}")
    }


    // current node is Context
    private fun AstNode<*>.buildContextModel(factory: SemanticModelFactory) = factory.contextScope {
        childs.forEach { child ->
            if (child.symbol is Elements) {
//                val elements = child.buildElements()
            }
        }
    }

//    private fun AstNode<*>.buildElements(): List<SemanticModel.Element> {
//        this
//    }


}