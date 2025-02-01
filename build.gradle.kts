import java.io.ByteArrayInputStream
import java.util.Scanner
import org.jetbrains.gradle.ext.ActionDelegationConfig
import org.jetbrains.gradle.ext.ActionDelegationConfig.TestRunner.PLATFORM
import org.jetbrains.gradle.ext.ProjectSettings
import org.jetbrains.gradle.ext.TaskTriggersConfig

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.idea.ext)
    antlr
    application
}

val antlrGeneratedSrc = layout.buildDirectory.dir("generated-src/antlr/main")
val antlrTasksGroup = "ANTLR"
val tetradPackage = "org.tetrad"
val tetradGeneratedSrc = tetradPackage.replace(".", "/")
    .let {
        antlrGeneratedSrc.get().dir(it)
    }

dependencies {
    antlr(libs.antlr)
    runtimeOnly(libs.antlr.runtime)
}

application {
    mainClass.set("$tetradPackage.MainKt")
}

sourceSets {
    main {
        java {
            srcDirs(antlrGeneratedSrc)
        }
    }
}

// See https://github.com/apple/pkl/blob/8cfd2357c6f0572e0f2eb27345d2feb94bcc31c2/pkl-core/pkl-core.gradle.kts#L119
val copyTokensForIntelliJAntlrPlugin by tasks.registering(Copy::class) {
    dependsOn(tasks.generateGrammarSource)
    into("src/main/antlr")
    from(tetradGeneratedSrc) {
        include("TetradLexer.tokens")
    }
}

// See https://github.com/apple/pkl/blob/8cfd2357c6f0572e0f2eb27345d2feb94bcc31c2/build.gradle.kts#L42
idea {
    project {
        this as ExtensionAware

        configure<ProjectSettings> {
            this as ExtensionAware

            configure<ActionDelegationConfig> {
                delegateBuildRunToGradle = true
                testRunner = PLATFORM
            }

            configure<TaskTriggersConfig> {
                afterSync(provider {
                    copyTokensForIntelliJAntlrPlugin
                })
            }
        }
    }
}

tasks.register<JavaExec>("grun") {
    val flag = project.findProperty("grunFlag")?.toString() ?: "-gui"
    val inputFilenames = project.findProperty("grunInputFilenames")
        ?.toString()
        ?.split(" ")
        .orEmpty()

    args = listOf(
        "${tetradPackage}.Tetrad",
        "document",
        flag
    ) + inputFilenames

    classpath = sourceSets.main.get().runtimeClasspath
    description = "grun { -gui | -tokens | -tree } [input-filename(s)]"
    group = antlrTasksGroup

    mainClass.set("org.antlr.v4.gui.TestRig")

    if (inputFilenames.isEmpty()) {
        doFirst {
            val scanner = Scanner(System.`in`)
            val input = scanner.nextLine()

            standardInput = ByteArrayInputStream(input.encodeToByteArray())
            scanner.close()
        }
    }
}

tasks.generateGrammarSource {
    arguments = arguments + listOf(
        "-package",
        tetradPackage
    )
    maxHeapSize = "64m"
    outputDirectory = tetradGeneratedSrc.asFile
}

tasks.compileKotlin {
    dependsOn(tasks.generateGrammarSource)
}

tasks {
    wrapper {
        gradleVersion = project.properties["gradle.version"].toString()
    }
}
