plugins {
    alias(libs.plugins.ktlint)
    alias(libs.plugins.better.junit.aggregator)
}

betterAggregator {
    outputDir.set("${project.layout.buildDirectory.dir("test-results/test").get().asFile.absolutePath}")
}

tasks.ktlintFormat {
    dependsOn(
        subprojects
            .filterNot(::isPlainDir)
            .map { "${it.path}:ktlintFormat" },
    )
}

tasks.ktlintCheck {
    dependsOn(
        subprojects
            .filterNot(::isPlainDir)
            .map { "${it.path}:ktlintCheck" },
    )
}

fun isPlainDir(project: Project) = !project.file("build.gradle").exists() && !project.file("build.gradle.kts").exists()

tasks.ktlintFormat {
    dependsOn(gradle.includedBuilds.map { it.task(":ktlintFormat") })
}

tasks.ktlintCheck {
    dependsOn(gradle.includedBuilds.map { it.task(":ktlintCheck") })
}

tasks.register("test") {
    dependsOn(
        subprojects
            .filterNot(::isPlainDir)
            .map { "${it.path}:test" },
    )
    dependsOn(gradle.includedBuilds.map { it.task(":test") })
    finalizedBy("aggregateJUnitXMLReports")
}

tasks.register("clean") {
    dependsOn(
        subprojects.filterNot(::isPlainDir)
            .map { "${it.path}:clean" },
    )
    dependsOn(gradle.includedBuilds.map { it.task(":clean") })
}
