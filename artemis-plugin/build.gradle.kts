plugins {
    // lang support
    kotlin("jvm") version "2.0.0-Beta3"
    java
    application

    // plugin support
    id("com.gradle.plugin-publish") version "1.2.0"
    publishing
    `maven-publish`
}

group = "wtf.zv.artemis"
version = "0.0.1"

repositories {
    mavenCentral()
}

val mainVerticleName = "wtf.zv.artemis.demo.EmbeddedDemo"
val launcherClassName = "io.vertx.core.Launcher"

val watchForChange = "src/**/*"
val doOnChange = "${projectDir}/gradlew classes"

tasks.withType<JavaExec> {
    args = listOf("run", mainVerticleName, "--redeploy=$watchForChange", "--launcher-class=$launcherClassName", "--on-redeploy=$doOnChange")
}

application {
    mainClass.set(launcherClassName)
}

dependencies {
    // plugin api
    implementation(gradleApi())
    implementation(kotlin("stdlib"))
    compileOnly("org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.21")

    // common
    implementation("com.google.guava:guava:32.1.1-jre")

    // poet
    implementation("com.squareup:kotlinpoet-ksp:1.14.2")

    // compiler
    implementation("org.jetbrains.kotlin:kotlin-compiler-embeddable:1.9.0")
}

gradlePlugin {
    val artemisPlugin by plugins.creating {
        id = "wtf.zv.artemis.plugin"
        version = "0.0.1"
        implementationClass = "wtf.zv.artemis.plugin.ArtemisCorePlugin"

        displayName = "artemis"
        description = "Generates bindings and transpiles KMP Kt JavaScript targeting code"
    }
}

publishing {
    publications {
        create<MavenPublication>("artemisCore") {
            from(components["java"])

            groupId = "wtf.zv.artemis"
            artifactId = "core"
            version = "0.0.1"
        }
    }
}

tasks {
    val artemisPublishPluginTask = "artemisPublish"

    //===== Publishers
    val publishArtemisPlugin = register(artemisPublishPluginTask) {
        group = "artemis"
        description = "Locally publishes the Artemis to ~/.m2/repositories"

        dependsOn(publishToMavenLocal)

        doLast {
            println("[Artemis @plugin]: NOTE: Artemis built and locally published to: ~/.m2/repositories")
        }
    }
}