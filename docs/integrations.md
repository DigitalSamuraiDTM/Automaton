# Integrations design (Fixed ideas)

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

The diagrams fix logical data flows. Kafka topics carry asynchronous commands and context updates; Common APIs provide synchronous access to task, test, and context information.

* [ASCII scheme](schemes/saas_ascii.md)
* [Mermaid scheme](schemes/saas_mermaid.md)

## Design with custom project integration

```

```

## Design with CI workflow
