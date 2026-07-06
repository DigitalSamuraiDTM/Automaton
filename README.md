# Automaton
Concept of compiler for UI tests

## General of concept

* **language** - context-free grammar for describing test
* **compiler** - parser, analyzer, semanthic generator
* **context** - declaration of screens, mocks, permissions, using in test
* **Intermediate representation (IR)** - represantion of test for generating at platform


### Principles

#### separation of syntax and semantics

Automaton language make only syntax validation. Checking of existing screens, ui elements, actions, mocks and other entries not relevant to syntax-analysis and perform only at semanthic analysis.

Then row:
```YAML
tap: loginButton
```
can be correctly event when the element "loginButton" is not exist

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
Test execution should not be used to detect errors that can be identified through syntactic or semantic analysis.

#### Single source of true

All project artifacts are derived from a single language program. Source text of program is a single place for describing logic scenaries. Executable code, documentation, allure description and other representations does not contain independent logic and must be automatically recreated at each compilation.

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
Adding new constructs requires changes to the grammar and a corresponding extension of semantic analysis.
Language extensions must not violate the correctness of existing programs, except in cases explicitly defined by the language specification.

#### Completeness of the subject model

The language describes not automation actions, but rather the testing domain model: screens, elements, actions, expectations, and so on. This means the language operates at the domain-level abstractions, and interaction details emerge only at the code generation stage.

#### Required context

Any automaton profram interpreted relative to some compilation context. Context is a part of program and worked particaptes in AST building.

## Context-free grammatic
Description of Automaton language based at classic theory of finite automates. The language is defined by four elements

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

* CompilationUnit - begin of all automaton programs
* Context - context for compilation of program. Describe screens, mocks, permissions and other
* Test - block with test scenario
* Metadata - test name, description, identifactors, categories, tags and etc of test
* StepList - service non terminal contains sequence of steps
* Step - named 

#### Middle-level (V2)
