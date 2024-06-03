plugins {
    // language support
    kotlin("multiplatform") version "2.0.0-Beta3"
    java
    application

    // plugin support
    id("maven-publish")

    // kotlin serialisation
    kotlin("plugin.serialization") version "1.9.22"

    // artemis
    // this is only required in order to get the artemisBuildJavaScript rule as part of the internal demo.
    // id("wtf.zv.artemis.plugin") version "0.0.1"
}

group = "wtf.zv.artemis"
version = "0.0.1"

repositories {
    mavenCentral()
    mavenLocal()
}

kotlin {
    jvm()

    js {
        browser()
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(kotlin("stdlib"))

                // kotlinx serialization
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")

                // kotlinx time
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.5.0")

                // kotlinx atomics
                implementation("org.jetbrains.kotlinx:atomicfu:0.24.0")

                // kotlinx coroutines
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0-RC")
            }
        }

        jsMain {
            dependencies {
                // ktor support
                implementation("io.ktor:ktor-client-core:2.3.8") // websocket
                implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.8")
            }
        }

        jvmMain {
            dependencies {
                // kotlin support
                implementation("org.jetbrains.kotlin:kotlin-reflect:2.0.0-Beta3")

                // vertx - http server
                implementation(project.dependencies.platform("io.vertx:vertx-stack-depchain:4.5.3"))
                implementation("io.vertx:vertx-web")
                implementation("io.vertx:vertx-lang-kotlin-coroutines")
                implementation("io.vertx:vertx-lang-kotlin")

                // kotlinx html + css
                implementation("org.jetbrains.kotlinx:kotlinx-html:0.8.1")
                implementation("org.jetbrains.kotlin-wrappers:kotlin-css:1.0.0-pre.604")

                // poet
                implementation("com.squareup:kotlinpoet-ksp:1.14.2")

                // logger
                implementation("ch.qos.logback:logback-classic:1.4.14")

                // common
                implementation("com.google.guava:guava:32.1.1-jre")

                // compiler
                implementation("org.jetbrains.kotlin:kotlin-compiler-embeddable:1.9.0")

                // netty support
                // TODO: This should be a downstream dependency
                implementation("io.netty:netty-resolver-dns-native-macos:4.1.106.Final:osx-aarch_64")
            }
        }

    }
}

publishing {
    publications {
        create<MavenPublication>("artemisCommon") {
            groupId = "wtf.zv.artemis"
            artifactId = "common"
            version = "0.0.1"

            from(components["kotlin"])
        }
    }
}

tasks {
    val artemisPublishPluginTask = "artemisCommonPublish"

    //===== Publishers
    val publishArtemisPlugin = register(artemisPublishPluginTask) {
        group = "artemis"
        description = "Locally publishes the Artemis Common library to ~/.m2/repositories"

        dependsOn(publishToMavenLocal)

        doLast {
            println("[Artemis @plugin]: NOTE: Artemis built and locally published to: ~/.m2/repositories.")
        }
    }
}