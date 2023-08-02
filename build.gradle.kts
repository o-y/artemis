tasks {
    register("BuildEverything") {
        group = "artemis"
        description = "Build "

        doLast {
            println("[Artemis KMP] Building everything")
        }
    }
}

// hide non-artemis tasks in the Intellij side-panel
//gradle.taskGraph.whenReady {
//    tasks
//        .filter { it.group != "artemis" }
//        .forEach { task ->
//            task.enabled = false
//            task.group = ""
//        }
//}