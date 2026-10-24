package com.digitalsamurai.automaton.semantic

import com.digitalsamurai.automaton.ast.Ast
import com.digitalsamurai.automaton.ast.AstData
import com.digitalsamurai.automaton.grammar.Actions
import com.digitalsamurai.automaton.grammar.Assert
import com.digitalsamurai.automaton.grammar.Condition
import com.digitalsamurai.automaton.grammar.Context
import com.digitalsamurai.automaton.grammar.Element
import com.digitalsamurai.automaton.grammar.Elements
import com.digitalsamurai.automaton.grammar.HttpRequest
import com.digitalsamurai.automaton.grammar.Input
import com.digitalsamurai.automaton.grammar.Metadata
import com.digitalsamurai.automaton.grammar.NonTerminal
import com.digitalsamurai.automaton.grammar.Permissions
import com.digitalsamurai.automaton.grammar.Requests
import com.digitalsamurai.automaton.grammar.Step
import com.digitalsamurai.automaton.grammar.Steps
import com.digitalsamurai.automaton.grammar.Tap
import com.digitalsamurai.automaton.grammar.Test
import com.digitalsamurai.automaton.grammar.Theme
import com.digitalsamurai.automaton.grammar.Wait
import com.digitalsamurai.automaton.grammar.toBoolean
import com.digitalsamurai.automaton.logger.api.AutomatonLogger
import com.digitalsamurai.automaton.semantic.SemanticAnalyzer.SemanticValue.*
import com.digitalsamurai.automaton.semantic.SemanticModel.Permission.*
import org.json.JSONObject
import java.net.URL

public class SemanticAnalyzer(private val logger: AutomatonLogger?) {


    suspend fun analyzeFull(data: AstData): SemanticModel {
        val astNode = data.ast
        if (astNode is Ast.Leaf<*>) {
            error("full analyze does not support leaf as root node")
        }
        var context: SemanticModel.Context? = null
        var test: SemanticModel.Test? = null
         (astNode as Ast.Node).childs.forEach { child ->
             // TODO сейчас работает потому что контекст стоит выше теста
             when (child.symbol) {
                 is Context -> {
                     context = child.buildContextModel() as SemanticModel.Context
                 }
                 is Test -> {
                    test = child.buildTestModel(context!!) as SemanticModel.Test
                 }
             }
         }
        return SemanticModel(
            requestId = data.requestId,
            context = context!!,
            test = test!!
        )
    }

    private fun Ast.buildTestModel(context: SemanticModel.Context): Any {
        return when (this) {
            is Ast.Leaf<*> -> this.analyzeLeaf()
            is Ast.Node -> this.buildTestNodeModel(context)
        }
    }


    // current node is Context
    private fun Ast.buildContextModel(): Any  {
       return when (this) {
           is Ast.Leaf<*> -> this.analyzeLeaf()
           is Ast.Node -> this.buildContextNodeModel()
       }
    }
    
    private fun Ast.Node.buildTestNodeModel(context: SemanticModel.Context): Any  {
        return when (this.symbol as NonTerminal) {
            Test -> {
                var steps: List<SemanticModel.Test.Step>? = null
                var metadata: SemanticModel.Test.Metadata? = null

                childs.forEach { child ->
                    when (val result = child.buildTestModel(context)) {
                        is SemanticValue.StepList -> steps = result.value
                        is SemanticModel.Test.Metadata -> metadata = result
                    }
                }

                SemanticModel.Test(
                    metadata = metadata!!,
                    steps = steps!!,
                )
            }
            Actions -> {
                val actionsList = mutableListOf<SemanticModel.Test.Action>()

                childs.forEach { child ->
                    when (val result = child.buildTestModel(context)) {
                        is SemanticModel.Test.Action -> actionsList.add(result)
                    }
                }
                return SemanticValue.ActionsList(actionsList)
            }
            Condition -> {
                var element: SemanticModel.Element? = null
                var propertyValue: String? = null
                var propertyName: String? = null
                childs.forEach { child ->
                    when (val result = child.buildTestModel(context)) {
                        // resolve element
                        is SemanticValue.ElementName -> element = context.elements.find { it.name == result.value }!!

                        is SemanticValue.PropertyValue -> propertyValue = result.value

                        is SemanticValue.PropertyName -> propertyName = result.value
                    }
                }
                return SemanticValue.Condition(element!!, propertyName!!, propertyValue!!)
            }
            Assert -> {
                var condition: SemanticValue.Condition? = null
                childs.forEach { child ->
                    when (val result = child.buildTestModel(context)) {
                        is SemanticValue.Condition -> condition = result
                        else -> error("Should not happen")
                    }
                }
                return SemanticModel.Test.Action.Assert(
                    element = condition!!.element,
                    property = SemanticModel.Element.Property.Visible(condition.propertyValue.toBoolean())
                )
            }
            Input -> {

                var element: SemanticModel.Element? = null
                var inputData: String? = null

                childs.forEach { child ->
                    when (val result = child.buildTestModel(context)) {
                        is SemanticValue.ElementName -> element = context.elements.find { it.name == result.value }!!
                        is SemanticValue.InputData -> inputData = result.value
                    }
                }
                return SemanticModel.Test.Action.Input(element!!, inputData!!)
            }
            Metadata -> {
                var testName: String? = null
                var id: String? = null

                childs.forEach { child ->
                    when (val result = child.buildTestModel(context)) {
                        is SemanticValue.TestName -> testName = result.value
                        is SemanticValue.TestId -> id = result.value
                    }
                }
                return SemanticModel.Test.Metadata(
                    testName = testName!!,
                    id = id!!,
                )
            }
            Step -> {
                var actions: List<SemanticModel.Test.Action>? = null
                var description: String? = null
                var requests: List<SemanticModel.Request> = emptyList()
                childs.forEach { child ->
                    when (val result = child.buildTestModel(context)) {
                        is SemanticValue.ActionsList -> actions = result.value
                        is SemanticValue.StepDescription -> description = result.value
                        is SemanticValue.StepRequests -> requests = result.value
                    }
                }
                return SemanticModel.Test.Step(
                    actions = actions!!,
                    description = description!!,
                    requests = requests
                )
            }
            Steps -> {
                val steps = mutableListOf<SemanticModel.Test.Step>()
                childs.forEach { child ->
                    when (val result = child.buildTestModel(context)) {
                        is SemanticModel.Test.Step -> steps.add(result)
                    }
                }

                return SemanticValue.StepList(steps)
            }
            Tap -> {
                var element: SemanticModel.Element? = null
                var type: SemanticModel.Test.Action.Tap.Type? = null
                childs.forEach { child ->
                    when (val result = child.buildTestModel(context)) {
                        is SemanticValue.ElementName -> element = context.elements.find { it.name == result.value }!!
                        is SemanticModel.Test.Action.Tap.Type -> type = result
                    }
                }

                return SemanticModel.Test.Action.Tap(
                    element = element!!,
                    type = type!!,
                )
            }
            Wait -> {
                var element: SemanticModel.Element? = null
                var timeout: kotlin.time.Duration? = null
                var property: SemanticModel.Element.Property? = null
                childs.forEach { child ->
                    when (val result = child.buildTestModel(context)) {
                        is SemanticValue.Condition -> {
                            element = result.element
                            property = SemanticModel.Element.Property.Visible(result.propertyValue.toBoolean())
                        }
                        is SemanticValue.Timeout -> {
                            timeout = result.value
                        }
                    }
                }
                return SemanticModel.Test.Action.Wait(
                    element = element!!,
                    property = property!!,
                    timeout = timeout!!
                )
            }
            Step.Requests -> {
                val requests = mutableListOf<SemanticModel.Request>()
                childs.forEach { child ->
                    when (val result = child.buildTestModel(context)) {
                        is RequestName -> {
                            val request = context.requests.find { it.name == result.value }!!
                            requests.add(request)
                        }
                    }
                }
                return StepRequests(requests)
            }
            else -> error("unexpected AST node ${this}")
        }
    }

    private fun Ast.Node.buildContextNodeModel(): Any  {
        return when (this.symbol as NonTerminal) {
            Context -> {
                var theme: SemanticModel.Context.Theme? = null
                var permissions: List<SemanticModel.Permission>? = null
                var elements: List<SemanticModel.Element>? = null
                var requests: List<SemanticModel.Request> = emptyList()

                this.childs.forEach { child ->
                    when (val result = child.buildContextModel()) {
                        is SemanticModel.Context.Theme -> theme = result
                        is SemanticValue.ElementsList -> elements = result.value
                        is SemanticValue.PermissionsList -> permissions = result.value
                        is SemanticValue.Requests -> requests = result.value
                        else -> error("unknown context node: $this")
                    }
                }

                SemanticModel.Context(
                    theme = theme,
                    permissions = permissions!!,
                    elements = elements!!,
                    requests = requests,
                )
            }
            Elements -> {
                val elements = mutableListOf<SemanticModel.Element>()
                this.childs.forEach { child ->
                    when (val result = child.buildContextModel()) {
                        is SemanticModel.Element -> elements.add(result)
                        else -> error("unknown context node: $this")
                    }
                }
                return SemanticValue.ElementsList(elements)
            }

            Permissions -> {
                val permissions = mutableListOf<SemanticModel.Permission>()
                this.childs.forEach { child ->
                    when (val result = child.buildContextModel()) {
                        is SemanticModel.Permission -> permissions.add(result)
                    }
                }
                return SemanticValue.PermissionsList(permissions)
            }
            Theme -> {
                var theme: SemanticModel.Context.Theme? = null
                childs.forEach { child ->
                    when (val result = child.buildContextModel()) {
                        is SemanticModel.Context.Theme -> theme = result
                    }
                }
                return theme!!
            }
            Element -> {
                var name: String? = null
                var layout: String? = null
                childs.forEach { child ->
                    when (val result = child.buildContextModel()) {
                        is SemanticValue.ElementName -> name = result.value
                        is SemanticValue.ElementLayout -> layout = result.value
                    }
                }
                return SemanticModel.Element(name!!, layout!!)
            }

            Requests -> {
                val requests = mutableListOf<SemanticModel.Request>()
                this.childs.forEach { child ->
                    when (val result = child.buildContextModel()) {
                        is SemanticModel.Request.Http -> requests.add(result)
                    }
                }
                return Requests(requests)
            }
            HttpRequest -> {
                var url: URL? = null
                var name: String? = null
                var body: SemanticModel.Request.Http.Body? = null
                this.childs.forEach { child ->
                    when (val result = child.buildContextModel()) {
                        is SemanticValue.RequestName -> name = result.value
                        is SemanticValue.RequestUrl -> url = result.value
                        is SemanticModel.Request.Http.Body -> body = result
                    }
                }
                return SemanticModel.Request.Http(
                    url = url!!,
                    name = name!!,
                    headers = mapOf(),
                    body = body!!
                )
            }
            HttpRequest.Body -> {
                var body: SemanticModel.Request.Http.Body? = null
                this.childs.forEach { child ->
                    when (val result = child.buildContextModel()) {
                        is HttpRequestPlainBody -> body = SemanticModel.Request.Http.Body.Plain(result.value)
                        is HttpRequestJsonBody -> body = SemanticModel.Request.Http.Body.Json(result.value)
                    }
                }
                return body!!
            }
            else -> error("unknown context node: $this")
        }
    }

    /**
     * Листья со своим типом сворачиваются в свой тип, а узлы с общим типом (например [String] или [Int]) сворачиваются
     * в семантическое значение, которое будет использовано родителем который по типу значения определит куда его проставить в модель
     */
    private fun Ast.Leaf<*>.analyzeLeaf(): Any {
        return when (this) {
            is Ast.Leaf.AstElementLayoutLeaf -> ElementLayout(this.value)
            is Ast.Leaf.AstElementNameLeaf -> ElementName(this.value)
            is Ast.Leaf.AstId -> TestId(this.value)
            is Ast.Leaf.AstCameraLeaf -> Camera(isGranted = this.value)
            is Ast.Leaf.AstMicrophoneLeaf -> Microphone(isGranted = this.value)
            is Ast.Leaf.AstTestName -> TestName(this.value)
            is Ast.Leaf.AstThemeMode -> when(this.value){
                Ast.Leaf.AstThemeMode.Mode.LIGHT -> SemanticModel.Context.Theme.LIGHT
                Ast.Leaf.AstThemeMode.Mode.DARK -> SemanticModel.Context.Theme.DARK
                Ast.Leaf.AstThemeMode.Mode.SYSTEM -> SemanticModel.Context.Theme.SYSTEM
            }
            is Ast.Leaf.AstTapType -> when(this.value) {
                Ast.Leaf.AstTapType.Type.SINGLE -> SemanticModel.Test.Action.Tap.Type.SINGLE
                Ast.Leaf.AstTapType.Type.DOUBLE -> SemanticModel.Test.Action.Tap.Type.DOUBLE
                Ast.Leaf.AstTapType.Type.LONG -> SemanticModel.Test.Action.Tap.Type.LONG
            }
            is Ast.Leaf.AstDuration -> Duration(value)
            is Ast.Leaf.AstElementProperty -> PropertyName(value)
            is Ast.Leaf.AstInputData -> InputData(value)
            is Ast.Leaf.AstPropertyValue -> PropertyValue(value)
            is Ast.Leaf.AstTimeout -> Timeout(value)
            is Ast.Leaf.AstStepDescription -> StepDescription(value)
            is Ast.Leaf.AstHttpRequestJsonBody -> HttpRequestJsonBody(value)
            is Ast.Leaf.AstHttpRequestName -> RequestName(value)
            is Ast.Leaf.AstHttpRequestPlainBody -> HttpRequestPlainBody(value)
            is Ast.Leaf.AstHttpRequestUrl -> RequestUrl(value)
        }
    }


    /**
     * Семантические значения нужны как внутренние посредники для сворачивания узлов деревьев
     * в семантическую модель через катаморфизм. Так как в некоторых узлах сворачивания у нас нет конечного значения
     * То нужен определенный посредник, который захватит текущее значение и передаст его родителю для дальнейшего сворачивания
     * Посредники реализованы строгой типизацией для тоо, чтобы родитель мог через каст определить какой узел был свернут и поставить значение
     */
    sealed interface SemanticValue {
        data class TestName(val value: String) : SemanticValue
        data class TestId(val value: String) : SemanticValue
        data class ElementName(val value: String) : SemanticValue
        data class ElementLayout(val value: String) : SemanticValue
        data class ElementsList(val value: List<SemanticModel.Element>) : SemanticValue
        data class StepList(val value: List<SemanticModel.Test.Step>) : SemanticValue
        data class PermissionsList(val value: List<SemanticModel.Permission>) : SemanticValue
        data class ActionsList(val value: List<SemanticModel.Test.Action>) : SemanticValue
        data class Duration(val value: kotlin.time.Duration) : SemanticValue
        data class Timeout(val value: kotlin.time.Duration) : SemanticValue
        data class PropertyValue(val value: String) : SemanticValue // TODO мигрировать на конкретные проперти, а не абстрактные
        data class PropertyName(val value: String) : SemanticValue // TODO мигрировать на конкретные проперти, а не абстрактные
        data class InputData(val value: String) : SemanticValue
        data class StepDescription(val value: String) : SemanticValue
        data class Condition(val element: SemanticModel.Element, val propertyName: String, val propertyValue: String) : SemanticValue
        data class Requests(val value: List<SemanticModel.Request>): SemanticValue
        data class RequestUrl(val value: URL): SemanticValue
        data class RequestName(val value: String) : SemanticValue
        data class HttpRequestPlainBody(val value: String) : SemanticValue
        data class HttpRequestJsonBody(val value: JSONObject) : SemanticValue
        data class StepRequests(val value: List<SemanticModel.Request>) : SemanticValue
    }
}