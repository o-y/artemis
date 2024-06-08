 plugins {
    // lang support
    kotlin("jvm") version "2.0.0-Beta3"
    java
    application

    // plugin support
    id("maven-publish")
}

group = "wtf.zv.artemis"
version = "0.0.1"

repositories {
    mavenCentral()
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
    //===== Publishers
    register(name = "artemisPublish") {
        group = "artemis"
        description = "Locally publishes the Artemis to ~/.m2/repositories"

        dependsOn(publishToMavenLocal)

        doLast {
            println("[Artemis @plugin]: NOTE: Artemis built and locally published to: ~/.m2/repositories")
        }
    }
}