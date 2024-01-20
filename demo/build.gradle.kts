plugins {
    kotlin("multiplatform") version "1.9.0"
    kotlin("plugin.serialization") version "1.9.0"

    java

    id("wtf.zv.artemis.plugin") version "0.0.1"
}

//apply<wtf.zv.artemis.plugin.ArtemisCorePlugin>()

group = "wtf.zv.artemis"
version = "0.0.1"

repositories {
    mavenLocal()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/kotlinx-html/maven")
    maven("https://maven.pkg.jetbrains.space/public/p/ktor/eap")
}

kotlin {
    jvm {
        mainRun {
            mainClass.set("wtf.zv.demo.DemoEntryPoint")
        }

        withJava()

        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
    }

    js(IR) {
        binaries.executable()
        browser {
            commonWebpackConfig(Action {
                output?.apply {
                    // https://webpack.js.org/configuration/output/#outputlibrarytarget
                    // 'this' populates the global context with the [library] declaration.
                    libraryTarget = "this"

                    // https://webpack.js.org/configuration/output/#outputfilename
                    outputFileName = "artemis_bundle.js"

                    // https://webpack.js.org/configuration/output/#outputlibrary
                    // JavaScript entrypoint: this["ARTEMIS_ENTRYPOINT"].
                    library = "ARTEMIS_ENTRYPOINT"
                }

                sourceMaps = false
                showProgress = true
            })
        }
    }

    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation("wtf.zv.artemis:core:0.0.1")

                // kotlinx html + css
                implementation("org.jetbrains.kotlinx:kotlinx-html:0.8.1")
                implementation("org.jetbrains.kotlin-wrappers:kotlin-css:1.0.0-pre.604")

                // common
                implementation("com.google.guava:guava:32.1.1-jre")
            }

            kotlin(Action {
                srcDirs("build/artemis/generated/main/kotlin")
            })
        }

        val jsMain by getting {
            dependencies {
                // std
                implementation(kotlin("stdlib-js"))

                // kotlinx html / browser
                implementation("org.jetbrains.kotlinx:kotlinx-html:0.8.1")
                implementation("org.jetbrains.kotlin-wrappers:kotlin-css:1.0.0-pre.604")

                // coroutines
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-js:1.7.3")

                // serialization
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-core-js:1.6.0-RC")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0-RC")
            }
        }
    }
}