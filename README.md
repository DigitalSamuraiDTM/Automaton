![logo](/docs/automaton.png)

# Automaton

Concept of compiler engine for UI tests

## Content

* [Architecture](/docs/architecture.md)

## General of concept

* **language** - context-free grammar for describing test
* **compiler** - parser, analyzer, semanthic generator
* **context** - declaration of screens, mocks, permissions, using in test
* **Intermediate representation (IR)** - represantion of test for generating at platform

### Principles

#### separation of syntax and semantics

Automaton language make only syntax validation. Checking of existing screens, ui elements, actions,
mocks and other entries not relevant to syntax-analysis and perform only at semanthic analysis.

Then row:

```YAML
tap: loginButton
```

can be correctly event when the element "loginButton" is not exist

#### Structure composition

Every composite non terminal is a container with named section. If a section has independent meaning
in the subject area or can potentially be expanded without changing the semantics of the container,
it is allocated as a separate non-terminal.

#### Single responsibility of semantics

Every syntax of language has single semantic

#### Concrete syntax

Context-free grammar of a language defines abstract structure of program.

Then:

* Language is not defined by YAML
* YAML is not defined by language
* changing of concrete syntax does not change language while preserving many production rules

#### Compilation determinism

With fixed compiler version, grammar and context the same program generates the same equivalent IR.

#### Compilation checks

Any error that can be detected before test execution must be detected by the compiler.d
Test execution should not be used to detect errors that can be identified through syntactic or
semantic analysis.

#### Single source of true

All project artifacts are derived from a single language program. Source text of program is a single
place for describing logic scenarios. Executable code, documentation, allure description and other
representations does not contain independent logic and must be automatically recreated at each
compilation.

#### Unified model

After successful syntax analysis, program transform to abstract syntax tree (AST).
After successful semantic analysis AST transform to intermediate representation (IR)
IR can build:

1) Executable Android test
2) Executable iOS test
3) Allure description (also known manually test)
4) Technical documentation
5) other representations

#### Language extensibility

Language extensions are permitted only by extending its formal model.
Adding new constructs requires changes to the grammar and a corresponding extension of semantic
analysis.
Language extensions must not violate the correctness of existing programs, except in cases
explicitly defined by the language specification.

#### Completeness of the subject model

The language describes not automation actions, but rather the testing domain model: screens,
elements, actions, expectations, and so on. This means the language operates at the domain-level
abstractions, and interaction details emerge only at the code generation stage.

#### Required context

Any automaton program interpreted relative to some compilation context. Context is a part of program
and worked participates in AST building.

#### Independence of actions

Each type of action is an independent non-terminal symbol of grammar and has its own system of
production rules.

## Context-free grammatic

Description of Automaton language based at classic theory of finite automates. The language is
defined by four elements

G=(V,Σ,P,S)

where:
V = finite set of non terminal symbols
Σ = finite set of terminal symbols
P = finite set of produce rules
S ∈ V = start symbol

also the classical conditions are met

V ∩ Σ = ∅ (non terminals not intercept with terminals)

and

S ∈ V (start symbol is one of non terminals symbols)

### Non terminals

Names of nested symbols are written as `Parent.Child` (for example `HttpRequest.Body`), `ε` is an empty production.
The *Representation* column is the key used in the YAML concrete syntax.
*Service* non-terminals have no own node in the AST: their productions are inlined into the productions of the parent
non-terminal when the parsing table is built.

| Non-terminal     | Representation | Service | Productions                                                                                                                                  | Description                                                                                                                                       |
|------------------|----------------|---------|:---------------------------------------------------------------------------------------------------------------------------------------------|:--------------------------------------------------------------------------------------------------------------------------------------------------|
| CompilationUnit  | compilationUnit | False  | CompilationUnit → Context Test                                                                                                               | Start symbol (S). Root of every Automaton program                                                                                                 |
| Context          | context        | False   | Context → Elements Requests Theme Permissions                                                                                                | Compilation context of the program: declares UI elements, network mocks, theme and permissions used by the test                                  |
| Elements         | elements       | False   | Elements → Element Elements <br/> Elements → ε                                                                                               | List of UI elements declared in the context. May be empty                                                                                         |
| Element          | element        | False   | Element → Element.Name Element.Layout                                                                                                        | Declaration of a UI element (screen, button, field, etc.): unique name and reference to its visual layout (Figma link or similar)                 |
| Requests         | requests       | False   | Requests → Request Requests <br/> Requests → ε                                                                                               | List of network request mocks declared in the context. May be empty                                                                               |
| Request          | request        | True    | Request → HttpRequest                                                                                                                        | Any kind of network request mock. Service symbol, extension point for other request types                                                         |
| HttpRequest      | http           | False   | HttpRequest → Request.Name Request.Url HttpRequest.RequestType HttpRequest.Headers HttpRequest.Body                                           | Mock of an HTTP request: name for referencing from steps, url, request method, headers and response body                                         |
| HttpRequest.Headers | headers     | False   | HttpRequest.Headers → ε                                                                                                                      | Headers of the mocked response. Not implemented yet, only the empty production exists. See [content type](#content-type-of-mocked-responses) |
| HttpRequest.Body | body           | False   | HttpRequest.Body → HttpRequest.Body.Plain <br/> HttpRequest.Body → HttpRequest.Body.Json                                                     | Body of the mocked HTTP response: plain text or JSON (object or array)                                                                            |
| Theme            | theme          | False   | Theme → ThemeMode <br/> Theme → ε                                                                                                            | Application theme for the test run (light, dark, system). Optional                                                                                |
| Permissions      | permissions    | False   | Permissions → Permission Permissions <br/> Permissions → ε                                                                                   | List of system permissions granted or denied for the test run. May be empty                                                                       |
| Permission       | permission     | True    | Permission → Microphone <br/> Permission → Camera                                                                                            | A single system permission. Service symbol                                                                                                        |
| Test             | test           | False   | Test → Metadata Test.Requests Steps                                                                                                          | Block with the test scenario                                                                                                                      |
| Test.Requests    | testRequests   | False   | Test.Requests → Request.Name Test.Requests <br/> Test.Requests → ε                                                                           | References (by name) to requests declared in the context, which are mocked during the whole test (general mocks). May be empty                   |
| Metadata         | metadata       | False   | Metadata → Id TestName                                                                                                                       | Test metadata: identifier and name (later: description, categories, tags, etc.)                                                                   |
| Steps            | steps          | False   | Steps → Step Steps <br/> Steps → ε                                                                                                           | Sequence of test steps                                                                                                                            |
| Step             | step           | False   | Step → Step.Description Step.Requests Actions                                                                                                | Named logical unit of the scenario: description, network mocks active during the step and the sequence of actions                                |
| Step.Requests    | stepRequests   | False   | Step.Requests → Request.Name Step.Requests <br/> Step.Requests → ε                                                                           | References (by name) to requests declared in the context, which are mocked while the step is executed. May be empty                              |
| Actions          | actions        | False   | Actions → Action Actions <br/> Actions → ε                                                                                                   | Sequence of actions inside a step                                                                                                                 |
| Action           | action         | True    | Action → Tap <br/> Action → Input <br/> Action → Wait <br/> Action → Assert                                                                  | Minimal semantic construct of the language that changes the state of the system under test or checks it. Service symbol                          |
| Tap              | tap            | False   | Tap → Element.Name TapType                                                                                                                   | User tap on a UI element. The element must be tappable                                                                                           |
| Input            | input          | False   | Input → Element.Name InputData                                                                                                               | User input of text into a UI element. The element must be inputtable                                                                              |
| Wait             | wait           | False   | Wait → Condition Timeout                                                                                                                     | Wait until the condition becomes true, but not longer than the timeout                                                                            |
| Assert           | assert         | False   | Assert → Condition                                                                                                                           | Immediate check of the condition without delays and timeouts                                                                                      |
| Condition        | condition      | False   | Condition → Element.Name ElementProperty PropertyValue                                                                                       | Expected state of a UI element: element reference, checked property and its expected value                                                        |

> `Delay → Duration` is not a part of the grammar now: it is commented out until it is decided whether it is a real action
> or a workaround for waiting.

### Terminal symbols

A terminal is a typed leaf of the AST: the lexical analyzer reads the key (*Representation*) and converts the following
value to the type of the terminal. A value that cannot be converted is a lexical error.

| Terminal              | Representation  | Value                                                    | Description                                                                                                                    |
|-----------------------|-----------------|:---------------------------------------------------------|:-------------------------------------------------------------------------------------------------------------------------------|
| Element.Name          | elementName     | string                                                   | Name of a UI element. In the context it declares the element, in actions and conditions it references an element declared in the context |
| Element.Layout        | elementLayout   | string                                                   | Reference to the visual representation of the element (Figma link or similar). Used in generated documentation and manual test cases |
| Request.Name          | requestName     | string                                                   | Name of a network request mock. In the context it declares the request, in `stepRequests`/`testRequests` it references a declared request |
| Request.Url           | url             | URL                                                      | Url of the mocked request. The path of the url is used for matching incoming requests                                         |
| HttpRequest.RequestType | requestType   | {get, post, put, delete}                                 | HTTP method used both for matching incoming requests and for generated mock rules                                            |
| HttpRequest.Body.Json | json            | path to a `.json` file                                   | JSON body of the mocked response. The file is read by the lexical analyzer and must contain a JSON object or array            |
| HttpRequest.Body.Plain| plain           | string                                                   | Plain text body of the mocked response                                                                                         |
| ThemeMode             | themeMode       | {light, dark, system}                                    | Application theme for the test run                                                                                             |
| Camera                | camera          | {true, false}                                           | Camera permission: granted (`true`) or denied (`false`)                                                                        |
| Microphone            | microphone      | {true, false}                                           | Microphone permission: granted (`true`) or denied (`false`)                                                                    |
| Id                    | id              | string                                                   | Unique identifier of the test (for example `AUTH_001`). Used in test management systems and manual test cases                  |
| TestName              | name            | string                                                   | Human-readable name of the test. Used to name generated test classes and files                                                 |
| Step.Description      | stepDescription | string                                                   | Human-readable description of a step. Becomes the step name in autotests and manual test cases                                 |
| InputData             | inputData       | string                                                   | Text that will be entered into a UI element. The element must be inputtable                                                    |
| TapType               | tapType         | {single}                                                 | Kind of tap. Only `single` is supported by the grammar now, `double` and `long` are planned. The element must be tappable      |
| Timeout               | timeout         | duration: numbers with units `d`, `h`, `m`, `s`, `ms`, `us`, `ns` separated by spaces, or ISO-8601 duration| Maximum time to wait for a condition in `wait`. Examples: `5s`, `1m 30s`, `PT5S`                                                                                 |
| Duration              | duration        | duration: numbers with units `d`, `h`, `m`, `s`, `ms`, `us`, `ns` separated by spaces, or ISO-8601 duration| Fixed time interval. Reserved for the `Delay` action, not used in productions now. Examples: `5s`, `1m 30s`, `PT5S`                                              |
| ElementProperty       | elementProperty | see [element properties](#element-properties)           | Name of the checked property of a UI element in a condition                                                                    |
| PropertyValue         | propertyValue   | see [element properties](#element-properties)           | Expected value of the checked property. The type depends on the property                                                       |

### Element properties

Properties describe the expected state of a UI element.
The `elementProperty` terminal contains the name of the property, `propertyValue` contains its expected value.

| Property | Values        | Description                                                                                                                                                                    |
|----------|:--------------|:-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| visible  | {true, false} | Defines whether the element should be visible on the screen. `true`: the element must be visible, `false`: the element must not be visible |

### High-level structure of a Automaton program:

[Example of non-terminals tree with test](examples/tree_example.md)

[Example of yaml specification at yaml](samples/tests/test_anonymous_auth.yaml)

## Semantic rules

Rules applied by the semantic analyzer. Some rules are checks: a program that breaks them is not compiled.
Other rules resolve values that are not written in the program explicitly.

#### Uniqueness of mocked requests

A network request can not be mocked by general test requests (`testRequests`) and step requests
(`stepRequests`) at the same time. Requests are the same when they have the same method and url path.
The mock server can not decide which response must be returned for such request, so there is no
priority between general and step requests: the intersection is a semantic error.

#### Content type of mocked responses

The body type of a mocked response defines its `Content-Type` header by default:

| Body type | Default `Content-Type` |
|-----------|------------------------|
| `json`    | `application/json`     |
| `plain`   | `text/plain`           |

An explicit `Content-Type` header of the response (when headers are supported by the grammar) overrides
the default value. Values are not checked for compatibility: a `json` body with `Content-Type: text/plain`
is a valid program, so a test can simulate a backend that returns a wrong content type.

**Warning.** A mocked response always has a `Content-Type` header. The negative scenario "backend returns a
response without `Content-Type`" can not be described now. Clients usually depend on this header to
deserialize the body (for example, ktor `ContentNegotiation` does not deserialize a response without it),
so the generated mock and the real backend may behave differently in this case.
