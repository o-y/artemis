//tasks {
//    val artemisRunTask = "artemisRun"
//
//    register(artemisRunTask) {
//        group = "artemis"
//        description = "Publishes Artemis, Builds JavaScript bindings, runs the server"
//
//        doLast {
//            println("[Artemis KMP] Building everything")
//        }
//    }
//}

//gradle.taskGraph.whenReady {
//    tasks
//        .filter { it.group != "artemis" }
//        .forEach { task ->
//            task.enabled = false
//            task.group = ""
//        }
//}