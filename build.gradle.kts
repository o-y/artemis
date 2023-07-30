plugins {
    kotlin("multiplatform") version "1.9.0"
    java
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/kotlinx-html/maven")
    maven("https://maven.pkg.jetbrains.space/public/p/ktor/eap")
}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "1.8"
        }

        // wtf
        mainRun {
            mainClass.set("demo.Main")
        }

        withJava()

        testRuns["test"].executionTask.configure {
            useJUnitPlatform();
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
                // kotlinx html + css
                implementation("org.jetbrains.kotlinx:kotlinx-html:0.8.1")
                implementation("org.jetbrains.kotlin-wrappers:kotlin-css:1.0.0-pre.555")

                // ktor - http server
                implementation("io.ktor:ktor-server-core-jvm:2.3.2")
                implementation("io.ktor:ktor-server-netty-jvm:2.3.2")
                implementation("io.ktor:ktor-server-status-pages-jvm:2.3.2")
                implementation("io.ktor:ktor-server-default-headers-jvm:2.3.2")
                implementation("io.ktor:ktor-server-html-builder-jvm:2.3.2")

                // logger
                implementation("ch.qos.logback:logback-classic:1.4.7")

                // compiler
                implementation("org.jetbrains.kotlin:kotlin-compiler-embeddable:1.3.21")
            }
        }

        val jsMain by getting {
            dependencies {
                // std
                implementation(kotlin("stdlib-js"))

                // kotlinx html / browser
                implementation("org.jetbrains.kotlinx:kotlinx-html:0.8.1")
            }
        }
    }
}

tasks {
    val artemisDist = "build/artemis_dist"

    val buildJavaScript = named("jsBrowserProductionWebpack")
    val updateYarn = named("kotlinUpgradeYarnLock")

    // Internal defs
    lateinit var copyJavaScriptTask: Task

    // Builders
    register("artemisBuildJavaScript") {
        group = "artemis"
        description = "Builds a JavaScript bundle from Kotlin code targeting JavaScript and places this in $artemisDist"

        copyJavaScriptTask.mustRunAfter(updateYarn)
        dependsOn(updateYarn, copyJavaScriptTask)

        doLast {
            println("[Artemis] JavaScript bundle built and placed in: ${project.name}/${artemisDist}")
        }
    }

    // Runners
    register("artemisRunServer") {
        group = "artemis"
        description = "Runs the JVM-targeting Kotlin server"

        val jvmRun = named("jvmRun").get()

        jvmRun.mustRunAfter(buildJavaScript)
        dependsOn(buildJavaScript, jvmRun)
    }

    // Internal defs
    copyJavaScriptTask = create<Copy>("artemisInternalCopyJavaScript") {
        group = ""
        description = "INTERNAL TASK - Copies the generated JavaScript bundle into $artemisDist"

        from(buildJavaScript) {
            exclude("webpack.config.js")
        }

        into(artemisDist)
    }
}