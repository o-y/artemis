rootProject.name = "artemis"

pluginManagement {
    repositories {
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
    }
}

 plugins {
     kotlin("multiplatform") version "2.0.0-Beta3" apply false
     kotlin("jvm") version "2.0.0-Beta3" apply false
 }

 include(
     "artemis-plugin",
     "artemis-common",
     "demo"
 )
