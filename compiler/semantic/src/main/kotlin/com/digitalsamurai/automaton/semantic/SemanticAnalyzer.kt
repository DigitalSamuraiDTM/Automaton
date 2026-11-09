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
import com.digitalsamurai.automaton.logger.api.AutomatonLogger
import com.digitalsamurai.automaton.semantic.SemanticAnalyzer.SemanticValue.*
import com.digitalsamurai.automaton.semantic.SemanticModel.Permission.*
import com.digitalsamurai.automaton.semantic.rules.SemanticRequestHeaderRule
import com.digitalsamurai.automaton.semantic.rules.SemanticRequestIntersectionRule
import org.json.JSONArray
import org.json.JSONObject
import java.net.URL

public class SemanticAnalyzer(private val logger: AutomatonLogger?) {

    private val requestHeaderRule = SemanticRequestHeaderRule()
    private val requestIntersectionRule = SemanticRequestIntersectionRule()


    suspend fun analyzeFull(data: AstData): SemanticModel {
        val astNode = data.ast
        if (astNode !is Ast.Node) {
            throw AutomatonSemanticException("Full analyze requires a root node, but got a leaf")
        }
        var context: SemanticModel.Context? = null
        var test: SemanticModel.Test? = null
        astNode.childs.forEach { child ->
            // TODO сейчас работает потому что контекст стоит выше теста
            when (child.symbol) {
                is Context -> {
                    val result = child.buildContextModel()
                    context = result as? SemanticModel.Context
                        ?: throw AutomatonSemanticException("Context node produced unexpected value: $result")
                }
                is Test -> {
                    val currentContext = requireSemanticValue(context, "Context before test analysis")
                    val result = child.buildTestModel(currentContext)
                    test = result as? SemanticModel.Test
                        ?: throw AutomatonSemanticException("Test node produced unexpected value: $result")
                }
            }
        }
        return SemanticModel(
            requestId = data.requestId,
            context = requireSemanticValue(context, "Compilation unit context"),
            test = requireSemanticValue(test, "Compilation unit test")
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
        return when (requireNonTerminal()) {
            Test -> {
                var steps: List<SemanticModel.Test.Step>? = null
                var metadata: SemanticModel.Test.Metadata? = null
                var generalRequests: Set<SemanticModel.Request> = emptySet()

                childs.forEach { child ->
                    when (val result = child.buildTestModel(context)) {
                        is StepList -> steps = result.value
                        is SemanticModel.Test.Metadata -> metadata = result
                        is TestRequests -> generalRequests = result.value.toSet()
                    }
                }
                val requiredSteps = requireSemanticValue(steps, "Test steps")
                requestIntersectionRule.resolve(generalRequests, requiredSteps)

                SemanticModel.Test(
                    metadata = requireSemanticValue(metadata, "Test metadata"),
                    steps = requiredSteps,
                    generalRequests = generalRequests,
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
                var propertyName: Ast.Leaf.AstElementProperty.Type? = null
                childs.forEach { child ->
                    when (val result = child.buildTestModel(context)) {
                        // resolve element
                        is ElementName -> element = context.elements.find { it.name == result.value }
                            ?: throw AutomatonSemanticException("Element '${result.value}' is not declared in context")

                        is PropertyValue -> propertyValue = result.value

                        is PropertyName -> propertyName = result.value
                    }
                }
                val property = when (requireSemanticValue(propertyName, "Condition property name")) {
                    Ast.Leaf.AstElementProperty.Type.VISIBLE -> SemanticModel.Element.Property.Visible(
                        parseBooleanPropertyValue("visible", requireSemanticValue(propertyValue, "Visible property value"))
                    )
                }
                return Condition(requireSemanticValue(element, "Condition element"), property)
            }
            Assert -> {
                var condition: SemanticValue.Condition? = null
                childs.forEach { child ->
                    when (val result = child.buildTestModel(context)) {
                        is SemanticValue.Condition -> condition = result
                        else -> throw AutomatonSemanticException("Assert contains unexpected value: $result")
                    }
                }
                val requiredCondition = requireSemanticValue(condition, "Assert condition")
                return SemanticModel.Test.Action.Assert(
                    element = requiredCondition.element,
                    property = requiredCondition.property
                )
            }
            Input -> {

                var element: SemanticModel.Element? = null
                var inputData: String? = null

                childs.forEach { child ->
                    when (val result = child.buildTestModel(context)) {
                        is ElementName -> element = context.elements.find { it.name == result.value }
                            ?: throw AutomatonSemanticException("Element '${result.value}' is not declared in context")
                        is InputData -> inputData = result.value
                    }
                }
                return SemanticModel.Test.Action.Input(
                    requireSemanticValue(element, "Input element"),
                    requireSemanticValue(inputData, "Input data")
                )
            }
            Metadata -> {
                var testName: String? = null
                var id: String? = null

                childs.forEach { child ->
                    when (val result = child.buildTestModel(context)) {
                        is TestName -> testName = result.value
                        is TestId -> id = result.value
                    }
                }
                return SemanticModel.Test.Metadata(
                    testName = requireSemanticValue(testName, "Test name"),
                    id = requireSemanticValue(id, "Test id"),
                )
            }
            Step -> {
                var actions: List<SemanticModel.Test.Action>? = null
                var description: String? = null
                var requests: List<SemanticModel.Request> = emptyList()
                childs.forEach { child ->
                    when (val result = child.buildTestModel(context)) {
                        is ActionsList -> actions = result.value
                        is StepDescription -> description = result.value
                        is StepRequests -> requests = result.value
                    }
                }
                return SemanticModel.Test.Step(
                    actions = requireSemanticValue(actions, "Step actions"),
                    description = requireSemanticValue(description, "Step description"),
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
                        is ElementName -> element = context.elements.find { it.name == result.value }
                            ?: throw AutomatonSemanticException("Element '${result.value}' is not declared in context")
                        is SemanticModel.Test.Action.Tap.Type -> type = result
                    }
                }

                return SemanticModel.Test.Action.Tap(
                    element = requireSemanticValue(element, "Tap element"),
                    type = requireSemanticValue(type, "Tap type"),
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
                            property = result.property
                        }
                        is Timeout -> {
                            timeout = result.value
                        }
                    }
                }
                return SemanticModel.Test.Action.Wait(
                    element = requireSemanticValue(element, "Wait element"),
                    property = requireSemanticValue(property, "Wait property"),
                    timeout = requireSemanticValue(timeout, "Wait timeout")
                )
            }
            Step.Requests -> {
                val requests = mutableListOf<SemanticModel.Request>()
                childs.forEach { child ->
                    when (val result = child.buildTestModel(context)) {
                        is RequestName -> {
                            val request = context.requests.find { it.name == result.value }
                                ?: throw AutomatonSemanticException("Request '${result.value}' is not declared in context")
                            requests.add(request)
                        }
                    }
                }
                return StepRequests(requests)
            }
            Test.Requests -> {
                val requests = mutableListOf<SemanticModel.Request>()
                childs.forEach { child ->
                    when (val result = child.buildTestModel(context)) {
                        is RequestName -> {
                            val request = context.requests.find { it.name == result.value }
                                ?: throw AutomatonSemanticException("Request '${result.value}' is not declared in context")
                            requests.add(request)
                        }
                    }
                }
                return TestRequests(requests)
            }
            else -> throw AutomatonSemanticException("Unexpected test AST node: $this")
        }
    }

    private fun Ast.Node.buildContextNodeModel(): Any  {
        return when (requireNonTerminal()) {
            Context -> {
                var theme: SemanticModel.Context.Theme? = null
                var permissions: List<SemanticModel.Permission> = emptyList()
                var elements: List<SemanticModel.Element>? = null
                var requests: List<SemanticModel.Request> = emptyList()

                this.childs.forEach { child ->
                    when (val result = child.buildContextModel()) {
                        is SemanticModel.Context.Theme -> theme = result
                        is ElementsList -> elements = result.value
                        is PermissionsList -> permissions = result.value
                        is SemanticValue.Requests -> requests = result.value
                        else -> throw AutomatonSemanticException("Context contains unexpected value: $result")
                    }
                }

                SemanticModel.Context(
                    theme = theme,
                    permissions = permissions,
                    elements = requireSemanticValue(elements, "Context elements"),
                    requests = requests,
                )
            }
            Elements -> {
                val elements = mutableListOf<SemanticModel.Element>()
                this.childs.forEach { child ->
                    when (val result = child.buildContextModel()) {
                        is SemanticModel.Element -> elements.add(result)
                        else -> throw AutomatonSemanticException("Elements contain unexpected value: $result")
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
                return requireSemanticValue(theme, "Context theme")
            }
            Element -> {
                var name: String? = null
                var layout: String? = null
                childs.forEach { child ->
                    when (val result = child.buildContextModel()) {
                        is ElementName -> name = result.value
                        is ElementLayout -> layout = result.value
                    }
                }
                return SemanticModel.Element(
                    requireSemanticValue(name, "Element name"),
                    requireSemanticValue(layout, "Element layout")
                )
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
                var requestType: SemanticModel.Request.Http.RequestType? = null
                var body: SemanticModel.Request.Http.Body? = null
                var headers: Map<String, String> = emptyMap()
                this.childs.forEach { child ->
                    when (val result = child.buildContextModel()) {
                        is RequestName -> name = result.value
                        is RequestUrl -> url = result.value
                        is HttpRequestType -> requestType = result.value
                        is SemanticModel.Request.Http.Body -> body = result
                        is ResponseHeaders -> headers = result.value
                    }
                }
                val requiredBody = requireSemanticValue(body, "HTTP request body")
                return SemanticModel.Request.Http(
                    url = requireSemanticValue(url, "HTTP request URL"),
                    name = requireSemanticValue(name, "HTTP request name"),
                    requestType = requireSemanticValue(requestType, "HTTP request type"),
                    headers = requestHeaderRule.resolve(requiredBody, headers),
                    body = requiredBody
                )
            }
            HttpRequest.Headers -> {
                // grammar has only the empty production now, explicit headers will be collected here
                return SemanticValue.ResponseHeaders(emptyMap())
            }
            HttpRequest.Body -> {
                var body: SemanticModel.Request.Http.Body? = null
                this.childs.forEach { child ->
                    when (val result = child.buildContextModel()) {
                        is HttpRequestPlainBody -> body = SemanticModel.Request.Http.Body.Plain(result.value)
                        is HttpRequestJsonBody -> body = result.value
                    }
                }
                return requireSemanticValue(body, "HTTP request body")
            }
            else -> throw AutomatonSemanticException("Unknown context AST node: $this")
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
            is Ast.Leaf.AstHttpRequestJsonBody -> {
                HttpRequestJsonBody(when(value) {
                    is Ast.Leaf.AstHttpRequestJsonBody.Type.Array -> SemanticModel.Request.Http.Body.JsonArray(value as JSONArray)
                    is Ast.Leaf.AstHttpRequestJsonBody.Type.Object -> SemanticModel.Request.Http.Body.JsonObject(value as JSONObject)
                })
            }
            is Ast.Leaf.AstHttpRequestName -> RequestName(value)
            is Ast.Leaf.AstHttpRequestType -> HttpRequestType(when (value) {
                Ast.Leaf.AstHttpRequestType.Type.GET -> SemanticModel.Request.Http.RequestType.GET
                Ast.Leaf.AstHttpRequestType.Type.POST -> SemanticModel.Request.Http.RequestType.POST
                Ast.Leaf.AstHttpRequestType.Type.PUT -> SemanticModel.Request.Http.RequestType.PUT
                Ast.Leaf.AstHttpRequestType.Type.DELETE -> SemanticModel.Request.Http.RequestType.DELETE
            })
            is Ast.Leaf.AstHttpRequestPlainBody -> HttpRequestPlainBody(value)
            is Ast.Leaf.AstHttpRequestUrl -> RequestUrl(value)
        }
    }

    /**
     * Проверяет, что узел AST относится к нетерминалу грамматики.
     *
     * @return нетерминал текущего узла.
     * @throws AutomatonSemanticException если символ узла не является нетерминалом.
     */
    private fun Ast.Node.requireNonTerminal(): NonTerminal {
        return symbol as? NonTerminal
            ?: throw AutomatonSemanticException("AST node contains a terminal where a non-terminal is required: $symbol")
    }

    /**
     * Возвращает обязательное значение семантического узла.
     *
     * @param value значение, полученное при сворачивании AST.
     * @param description описание значения для сообщения об ошибке.
     * @return ненулевое значение семантического узла.
     * @throws AutomatonSemanticException если значение отсутствует.
     */
    private fun <T> requireSemanticValue(value: T?, description: String): T {
        return value ?: throw AutomatonSemanticException("Missing required semantic value: $description")
    }

    /**
     * Преобразует строковое значение Boolean-property в Boolean.
     *
     * @param propertyName имя property для сообщения об ошибке.
     * @param value исходное строковое значение property.
     * @return true или false, записанное в property value.
     * @throws AutomatonSemanticException если значение отличается от true или false.
     */
    private fun parseBooleanPropertyValue(propertyName: String, value: String): Boolean {
        return when (value) {
            "true" -> true
            "false" -> false
            else -> throw AutomatonSemanticException("Property '$propertyName' expects 'true' or 'false', but got '$value'")
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
        data class PropertyValue(val value: String) : SemanticValue
        data class PropertyName(val value: Ast.Leaf.AstElementProperty.Type) : SemanticValue
        data class InputData(val value: String) : SemanticValue
        data class StepDescription(val value: String) : SemanticValue
        data class Condition(val element: SemanticModel.Element, val property: SemanticModel.Element.Property) : SemanticValue
        data class Requests(val value: List<SemanticModel.Request>): SemanticValue
        data class RequestUrl(val value: URL): SemanticValue
        data class RequestName(val value: String) : SemanticValue
        data class HttpRequestType(val value: SemanticModel.Request.Http.RequestType) : SemanticValue
        data class HttpRequestPlainBody(val value: String) : SemanticValue
        data class HttpRequestJsonBody(val value: SemanticModel.Request.Http.Body) : SemanticValue
        data class StepRequests(val value: List<SemanticModel.Request>) : SemanticValue
        data class TestRequests(val value: List<SemanticModel.Request>) : SemanticValue
        data class ResponseHeaders(val value: Map<String, String>) : SemanticValue
    }
}