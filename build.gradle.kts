plugins {
    alias(libs.plugins.ktlint)
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

tasks.register("clean") {
    dependsOn(
        subprojects.filterNot(::isPlainDir)
            .map { "${it.path}:clean" },
    )
    dependsOn(gradle.includedBuilds.map { it.task(":clean") })
}
