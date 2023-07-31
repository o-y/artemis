plugins {
    // lang support
    kotlin("jvm") version "1.9.0"
    java

    // plugin support
    id("com.gradle.plugin-publish") version "1.2.0"
}

group = "wtf.zv.artemis.compiler"
version = "0.0.1"

gradlePlugin {
    val artemisPlugin by plugins.creating {
        id = "wtf.zv.artemis.compiler"
        version = "0.0.1"
        implementationClass = "wtf.zv.artemis.compiler.ArtemisCompilerPlugin"

        displayName = "artemis"
        description = "Generates bindings for JavaScript-targeting multiplatform code"
    }
}

repositories {
    mavenCentral()
}

dependencies {
    // plugin api
    implementation(gradleApi())
    implementation(kotlin("stdlib"))

    // multiplatform
    compileOnly("org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.21")

    // compiler
    implementation("org.jetbrains.kotlin:kotlin-compiler-embeddable:1.9.0")

    // common
    implementation("com.google.guava:guava:32.1.1-jre")

    // poet
    implementation("com.squareup:kotlinpoet-ksp:1.14.2")
}

tasks {
    val publishToMavenLocal = named("publishToMavenLocal")

    //===== Publishers
    val publishArtemisPlugin = register("artemisPublishPlugin") {
        group = "artemis"
        description = "Locally publishes the ArtemisCompilerPlugin to ~/.m2/repositories"

        dependsOn(publishToMavenLocal)

        doLast {
            println("[Artemis] ArtemisCompilerPlugin built and locally published to: ~/.m2/repositories")
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