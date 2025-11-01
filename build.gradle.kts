plugins {
    alias(libs.plugins.ktlint)
}

tasks.ktlintFormat {
    dependsOn(
        subprojects
            .filterNot(::isPlainDir)
            .map { "${it.path}:ktlintFormat" },
    )
    dependsOn()
}

tasks.ktlintCheck {
    dependsOn(
        subprojects
            .filterNot(::isPlainDir)
            .map { "${it.path}:ktlintCheck" },
    )
    dependsOn()
}

fun isPlainDir(project: Project) = !project.file("build.gradle").exists() && !project.file("build.gradle.kts").exists()

tasks.named("ktlintFormat") {
    dependsOn(gradle.includedBuilds.map { it.task(":ktlintFormat") })
}

tasks.named("ktlintCheck") {
    dependsOn(gradle.includedBuilds.map { it.task(":ktlintCheck") })
}
