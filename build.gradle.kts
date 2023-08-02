plugins {
    kotlin("multiplatform") version "1.9.0"
    java

    id("wtf.zv.artemis.compiler") version "0.0.1"
}

apply<wtf.zv.artemis.compiler.ArtemisCompilerPlugin>()

group = "wtf.zv.artemis"
version = "0.0.1"

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

                // coroutines
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-js:1.7.3")
            }
        }
    }
}

tasks {
    val artemisDist = "build/artemis/javascript"
    val demoEntryPoint = "demo.DemoEntryPoint"

    val buildJavaScript = named("jsBrowserProductionWebpack")
    val updateYarn = named("kotlinUpgradeYarnLock")

    // Internal defs
    lateinit var copyJavaScriptTask: Task

    //===== Builders
    val artemisBuildJavaScript = register("artemisBuildJavaScript") {
        group = "artemis"
        description = "Generates a JavaScript bundle from Kotlin code targeting JavaScript and places this in $artemisDist"

        copyJavaScriptTask.mustRunAfter(updateYarn)
        dependsOn(updateYarn, copyJavaScriptTask)

        doLast {
            println("[Artemis] JavaScript bundle built and placed in: ${project.name}/${artemisDist}")
        }
    }

    //===== Runners
    val artemisRunServer = register<JavaExec>("artemisRunServer") {
        group = "artemis"
        description = "Runs the JVM-targeting Kotlin server"

        dependsOn(artemisBuildJavaScript)

        mainClass.set(demoEntryPoint)
        classpath = sourceSets["main"].runtimeClasspath
    }

    //===== Internal defs
    copyJavaScriptTask = create<Copy>("artemisInternalCopyJavaScript") {
        group = ""
        description = "INTERNAL TASK - Copies the generated JavaScript bundle into $artemisDist"

        from(buildJavaScript) {
            exclude("webpack.config.js")
            include("*.js", "*.html")
        }

        into(artemisDist)
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