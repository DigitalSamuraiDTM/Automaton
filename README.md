# Automaton

Concept of compiler for UI tests

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

#### Single responsibility of semanthics

Every syntax of language has single semanthic

#### Concrete syntax

Context-free grammar of a language defines abstract structure of program.

Then:

* Language is not defined by YAML
* YAML is not defined by language language
* changing of concrete syntax does not change language while preserving many production rules

#### Compilation determenism

With fixed compiler version, grammar and context the same program generates the same equivalent IR.

#### Compilation checks

Any error that can be detected before test execution must be detected by the compiler.d
Test execution should not be used to detect errors that can be identified through syntactic or
semantic analysis.

#### Single source of true

All project artifacts are derived from a single language program. Source text of program is a single
place for describing logic scenaries. Executable code, documentation, allure description and other
representations does not contain independent logic and must be automatically recreated at each
compilation.

#### Unified model

After successfull syntax analysis, program transform to abstract syntax tree (AST).
After successfull semanthic analysis AST transform to intermediate representation (IR)
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

Any automaton profram interpreted relative to some compilation context. Context is a part of program
and worked particaptes in AST building.

#### Indepence of actions

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

| Non-terminal    | Service | Productions                                                                                                      |                                                                   Description                                                                   |
|-----------------|---------|:-----------------------------------------------------------------------------------------------------------------|:-----------------------------------------------------------------------------------------------------------------------------------------------:|
| CompilationUnit | False   | CompilationUnit → Context Test                                                                                   |                                                         Begin of all automaton programs                                                         |
| Context         | False   |                                                                                                                  |                               context for compilation of program. Describe screens, mocks, permissions and other                                |
| Test            | False   | Test → Metadata StepList                                                                                         |                                                            block with test scenario                                                             |
| Metadata        | False   | CompilationUnit → Context Test                                                                                   |                                     test name, description, identifactors, categories, tags and etc of test                                     |
| StepList        | False   | StepList → Step <br/> StepList → Step StepList                                                                   |                                                 Service non terminal contains sequence of steps                                                 |
| Step            | False   | Step → ActionList                                                                                                |                                               named logic unit of scenario, unites of a sequence                                                |
| ActionList      | False   | ActionList → Action <br/> ActionList → Action ActionList                                                         |                                                         Begin of all automaton programs                                                         |
| Action          | True    | Action → Tap <br/>Action → Input <br/>Action → Wait <br/>Action → Delay <br/>Action → Delay <br/>Action → Assert | a minimal semantic construct of a language that describes a change in the state of the system under test or a check of the state of the system. |
| Tap             | False   | ElementReference TapType                                                                                         |                                                          user action tap on ui element                                                          |
| Input           | False   | Input → ElementReference InputData                                                                               |                                                          User action input data at ui                                                           |
| Wait            | False   | Wait → Condition Timeout                                                                                         |                                                          Await some times for loading                                                           |
| Assert          | False   | Assert → Condition                                                                                               |                                              Check required condition without delays and timeouts                                               |
| Condition       | False   | Condition → ElementReference ElementProperty PropertyValue                                                       |                                                    Ui element reference, setuped in context                                                     |
| Delay           | False   | Delay → Duration                                                                                                 |                                                    Ui element reference, setuped in context                                                     |

### Terminal symbols

| Non-terminal     | Value                          |                                 Description                                 |
|------------------|:-------------------------------|:---------------------------------------------------------------------------:|
| ElementReference | CompilationUnit → Context Test |                  Ui element reference declared in context                   |
| InputData        | CompilationUnit → Context Test | Symbols, that will be inputed at ui element. The element must be inputtable |
| TapType          | CompilationUnit → Context Test |                        The element must be tappable                         |
| Timeout          | CompilationUnit → Context Test |                                    TODO                                     |
| Duration         | CompilationUnit → Context Test |                                    TODO                                     |
| ElementProperty  | CompilationUnit → Context Test |                                    TODO                                     |
| PropertyValue    | CompilationUnit → Context Test |                                    TODO                                     |

### High-level structure of a Automaton program:

[Example of non-terminals tree with test](examples/tree_example.md)

[Example of yaml specification at yaml](examples/tree_example.yaml)
