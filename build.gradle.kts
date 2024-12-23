import java.io.ByteArrayInputStream
import java.util.Scanner

plugins {
    alias(libs.plugins.kotlin.jvm)
    antlr
    application
}

val antlrTasksGroup = "ANTLR"
val tetradPackage = "com.objp.tetrad"

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
            srcDirs("${layout.buildDirectory}/generated-src/antlr/main")
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
        "init",
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

tasks.compileKotlin {
    dependsOn(tasks.generateGrammarSource)
}

tasks.generateGrammarSource {
    maxHeapSize = "64m"
    arguments = arguments + listOf(
        "-package",
        tetradPackage,
    )
}

tasks {
    wrapper {
        gradleVersion = project.properties["gradle.version"].toString()
    }
}
