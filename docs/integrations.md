# Integrations design (Concept)

```                                          
┌────────────────────┐           ┌────────────────────┐            ┌────────────────────┐                          
│                    │           │                    │            │                    │                         
│    Entry point     │  ──────▶  │    Core logic      │  ──────▶   │      Provider      │                                  
│                    │           │                    │            │                    │                             
└────────────────────┘           └────────────────────┘            └────────────────────┘                          
```

### Entry points

One of the way to work with automaton logic can be

* Web service

Used for integration over automaton SaaS, deployed at internal company hosting.

* CLI

Library scripts used for CI integration or custom services with automaton logic

* SDK (Kotlin library for developers)

Instrumentation library with API for low level integrations over code



### Core logic


## Design with all automaton infrastructure (SaaS)

```               
                                                                                                                                                
                                        Automaton environment                                                           Internal project environment                                      
 ┌ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ┐  ┌ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─  ┐                                                                                                                                                                                    
                                                                                                                                                                                                            
 │                                    ┌────────────────────┐            ┌────────────────────┐       │  │                                                        │                                                           
                                      │     Automaton      │            │     Automaton      │                         Webhook                                                                                      
 │             ┌───────────────────▶  │     Autotest       │  ──────▶   │   autotest result  │  ◀────│──│───────────────────────────────────────┐                │                                                       
               │                      │     Operations     │            │     collector      │                                                  │                                              
 │             │                      └────────────────────┘            └────────────────────┘       │  │                                       │                │                                                                    
               │                                                                  ▼                                                             │                                                                          
 │   ┌────────────────────┐                                             ┌────────────────────┐       │   │                                      │                │                                                           
     │                    │                                             │                    │                                                  │                                                                     
 │   │    Web point       │                                             │      Autotest      │       │   │           ┌────────────────────┐     │                │                            
     │                    │                                             │      Storage       │                       │  ios autotesting   │     │                                                                                                                                                               
 │   └────────────────────┘                                             └────────────────────┘       │   │   ┌────▶  │   platform with    │  ───┤                │                                                                                                                             
               │                                                                  ▲                          │       │ Automaton provider │     │                                                                                                                                      
 │             │                      ┌────────────────────┐            ┌────────────────────┐       │   │   │       └────────────────────┘     │                │                                                                                                                              
               │                      │                    │            │     Automaton      │               │                                  │                                                                                                                                                                 
 │             └───────────────────▶  │      Compiler      │  ──────▶   │      linked        │  ─────│──│────┤                                  │                │                                                                                                                                                                                           
                                      │                    │            │      provider      │               │       ┌────────────────────┐     │                                                                                                                                                                                        
 │                                    └────────────────────┘            └────────────────────┘       │   │   │       │Android autotesting │     │                │                                                                                                                                                                          
                                                                                                             ├────▶  │   platform with    │ ────┤                                                                                                                                                                                              
 └ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ┘   │   │       │ Automaton provider │     │                │                                                                                                                                                                                               
                                                                                                             │       └────────────────────┘     │                                                                                                        
                                                                                                         │   │                                  │                │                             
                                                                                                             │                                  │                                                             
                                                                                                         │   │       ┌────────────────────┐     │                │                                                                   
                                                                                                             │       │Android autotesting │     │                                                                                   
                                                                                                         │   └────▶  │   platform with    │ ────┘                │                                                                            
                                                                                                                     │ Automaton provider │                                                                                        
                                                                                                         │           └────────────────────┘                      │                                                                                                    
                                                                                                                                                                                                                        
                                                                                                         └ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ┘                  
```


## Design with custom project integration

```

























```

## Design with CI workflow