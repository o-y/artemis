Artemis - Kotlin-based server side rendering framework
------------------------------------------------------

Artemis is an opinionated framework for creating toy websites and web apps. Artemis leverages the Kotlin multiplatform [1] (KMP) which allows Kotlin code to target both the browser using the Kotlin/JS IR compiler [2] and the JVM with a number of DSLs [3] to support writing HTML [4] and CSS [5] in pure Kotlin.

The KMP project offers first class transpilation/browser support for most packages from the Kotlin and Java standard libraries [6] and also provides a number of auto-generated JavaScript bindings [7] and browser entrypoints [8] using Dukat [9] and interoperability-focused APIs [10] to support depending on JavaScript/TypeScript libraries from Kotlin projects [11].

The long-term vision of Artemis is to expose a framework for writing progressively enhanced server-side rendered (SSR) with optional support for client-side interactivity provided by Kotlin KMP.

planned features (unordered):
  - transition to the Kotlin K2 compiler [12] once it fully supports KMP.
  - add Web Assembly (WASM) compilation support [13], waiting on the w3c Garbage Collector standard [14] finalisation.
  - provide Protocol Buffer [15] (or some other language-agnostic serialization format) to pass arguments to the client.
  - provide a websocket API for client JavaScript to communicate with the server, likely also using Protobufs.
  - improve HTML + Kt/JS interoperability by:
    - generate bindings for Kt/JS to access JVM-defined Document Elements in a type-safe manner.
    - provide a wrapper around the aforementioned features to simplify writing reactive applications.
  - various performance optimisations:
    - introduce the notion of "pure" server-side rendered Kotlin pages which can be indefinitely cached.
    - multi-thread certain APIs and execute page rendering in coroutine scopes
    - introduce a File target so entirely pure projects can generate HTML files without the need for a web server.

project structure:
  - artemis
    - core       - core library
    - compiler   - plugin support; generates JVM-targeted bindings from KMP JS code and transpiles into JavaScript
    - plugin     - Gradle plugin; registers client-facing Gradle tasks and the compiler plugin
  - artemis-common
    -
  - demo
    - commonMain - KMP Shared objects (specifically HTML Element IDs)
    - jvmMain    - KMP JVM-based demo
    - jsMain     - KMP JS-targeting Kotlin demo

references:
  - 1: https://kotlinlang.org/docs/multiplatform.html
  - 2: https://kotlinlang.org/docs/js-ir-compiler.html
  - 3: http://catb.org/~esr/writings/taoup/html/minilanguageschapter.html
  - 4: https://github.com/Kotlin/kotlinx.html
  - 5: https://github.com/JetBrains/kotlin-wrappers/tree/master/kotlin-css/src/commonMain/kotlin/kotlinx/css
  - 6: https://kotlinlang.org/api/latest/jvm/stdlib/
  - 7: https://github.com/JetBrains/kotlin/tree/master/libraries/stdlib/js
  - 8: https://kotlinlang.org/api/latest/jvm/stdlib/kotlinx.browser/
  - 9: https://github.com/Kotlin/dukat
  - 10: https://kotlinlang.org/docs/js-to-kotlin-interop.html#kotlin-types-in-javascript
  - 11: https://kotlinlang.org/docs/using-packages-from-npm.html
  - 12: https://blog.jetbrains.com/kotlin/2023/02/k2-kotlin-2-0
  - 13: https://kotlinlang.org/docs/wasm-overview.html
  - 14: https://github.com/WebAssembly/gc
  - 15: https://github.com/protocolbuffers/protobuf