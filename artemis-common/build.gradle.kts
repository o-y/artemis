plugins {
    // language support
    kotlin("multiplatform") version "1.9.0"
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
    mavenLocal()
}

kotlin {
    jvm()

    js {
        browser()
    }

    sourceSets {
        withSourcesJar(publish = true)

        commonMain {
            dependencies {
                implementation(kotlin("stdlib"))
            }
        }

        js {
            dependencies {
                // kotlinx html / css
                implementation("org.jetbrains.kotlinx:kotlinx-html:0.8.1")
                implementation("org.jetbrains.kotlin-wrappers:kotlin-css:1.0.0-pre.604")
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
            println("[Artemis @plugin]: NOTE: Artemis built and locally published to: ~/.m2/repositories")
        }
    }
}