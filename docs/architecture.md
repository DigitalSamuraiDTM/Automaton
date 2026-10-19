# Architecture

General concept is a compiler style with several flow of input data analyze

#### Scheme

## General scheme of compiler

```

                            Frontend                               
      ┌─────────────────────────────────────────────────────┐  Frontend is a first element of compiler. It provide input data based on different
      │                 Input data flow                     │  developer flow. For example it can be: YAML data or REST API with custom requests
      │                        │                            │  
      │                        ▼                            │
      │                Lexical analyzer                     │  Lexical analyzer transform input data (yaml file, http request body, etc) and validate
      │                        │                            │  it to Grammar symbols. Lexical analyzer must strictly define the symbols for next steps
      │                        ▼                            │
      │              Validated symbol flow                  │  At finish of working first module is a validated symbols flow based on Automaton grammar
      │                                                     │
      └─────────────────────────────────────────────────────┘ 
                               │
                               ▼
                           AST Module                           AST module transform symbols flow to strongly typized tree structure, using Automaton 
       ┌─────────────────────────────────────────────────────┐  grammar rules.
       │                 Prepare Parser                      │  Some parsers like LL1 need preparing step. At this step parser read all grammar rules
       │                        │                            │  and transform it to other structure convient for the parser algorithm
       │                        ▼                            │
       │             Build strong typized tree               │  Parser read symbol by symbol from flow and build tree structure.
       │                                                     │
       │                                                     │
       │                                                     │
       │                                                     │
       └─────────────────────────────────────────────────────┘       
                                │                   
                                ▼            
                          Semantic module                            
       ┌─────────────────────────────────────────────────────┐  Semantic analyze use semantic rules and resolve elements references       
       │                Build test context                   │  At first step it build test semantic context for resolving ui elements, requests and other
       │                        │                            │  test elements
       │                        ▼                            │
       │             Execute semantic checks                 │  After building correctly context semantic analyzer begins build semantic model and use other
       │                        │                            │  semantic checks (like check "if element exist in context" or check "if element unused in context")
       │                        ▼                            │
       │             Build test semantic model               │  After analyze analyzer build semantic model convenient for use in the backends.
       │                                                     │
       └─────────────────────────────────────────────────────┘ 
                                │ 
           Static execution     │       Dynamic execution                       
              ┌─────────────────┴────────────────────────────┐                                                                                   
              │                                              │                                                       
              ▼                                              ▼             
    ┌────────────────────┐                        ┌────────────────────┐                                 
    │ Manual Test cases  │                        │                    │                                 
    │ AndroidKaspresso   │                        │                    │                                 
    │ AllureTestOpsTest  │                        │        JIT         │  (now concept only)                           
    │  IOs Kif test      │                        │                    │                                 
    │                    │                        │                    │                               
    └────────────────────┘                        └────────────────────┘                                          
                  
                  
                  
                  
                 

```