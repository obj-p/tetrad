plugins {
    alias(libs.plugins.kotlin.jvm)
    antlr
    application
}

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
            srcDirs("build/generated-src/antlr/main")
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
