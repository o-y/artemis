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
    js()

    sourceSets {
        all {
            withSourcesJar(publish = true)

            dependencies {
                implementation(kotlin("stdlib"))
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