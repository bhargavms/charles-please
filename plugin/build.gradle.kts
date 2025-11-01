plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.java.gradle.plugin)
    alias(libs.plugins.maven.publish)
    alias(libs.plugins.gradle.plugin.publish)
    alias(libs.plugins.ktlint)
}

group = "dev.mogra"
version = "1.0.0"

dependencies {
    // Test dependencies
    testImplementation(libs.bundles.kotlin.test)
    testImplementation(libs.bundles.test)
}

gradlePlugin {
    plugins {
        create("charlesPlease") {
            id = "dev.mogra.charlesplease"
            implementationClass = "dev.mogra.charlesplease.CharlesPleasePlugin"
            displayName = "Charles Please"
            description =
                "Gradle plugin to automatically configure Android device proxy settings via ADB for Charles Proxy"
        }
    }
}

// Plugin bundle configuration (commented out for testing)
// pluginBundle {
//     website = "https://github.com/mogra/charlesplease"
//     vcsUrl = "https://github.com/mogra/charlesplease.git"
//     tags = listOf("android", "adb", "proxy", "charles", "debugging")
// }

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(libs.versions.java.get().toInt()))
    }
}

tasks.test {
    useJUnitPlatform()
}

// Handle duplicate files
tasks.processResources {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

// Kotlin JVM toolchain
kotlin {
    jvmToolchain(libs.versions.java.get().toInt())
}

// ktlint configuration
ktlint {
    version.set("1.2.1")
    debug.set(true)
    verbose.set(true)
    android.set(false)
    outputToConsole.set(true)
    outputColorName.set("RED")
    ignoreFailures.set(false)
    enableExperimentalRules.set(false)

    filter {
        exclude("**/generated/**")
        include("**/kotlin/**")
    }
}
